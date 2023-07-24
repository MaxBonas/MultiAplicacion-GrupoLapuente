package MultiAplicacion.controllers;


import MultiAplicacion.DTOs.*;
import MultiAplicacion.ENUMs.Turno;
import MultiAplicacion.controllers.interfaces.AdminControllerInterface;
import MultiAplicacion.entities.*;
import MultiAplicacion.repositories.*;
import MultiAplicacion.services.*;
import MultiAplicacion.services.interfaces.TareaCumplidaServiceInterface;
import MultiAplicacion.services.interfaces.TareaServiceInterface;
import MultiAplicacion.services.interfaces.UbicacionServiceInterface;
import MultiAplicacion.services.interfaces.WorkerServiceInterface;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.webjars.NotFoundException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;

@Controller
// La anotación @Controller indica que esta clase es un controlador de Spring MVC.
@RequestMapping("/admin/{sociedadId}")
// La anotación @RequestMapping define la ruta base que este controlador manejará.
public class AdminController implements AdminControllerInterface {
    // Las anotaciones @Autowired son utilizadas para la inyección automática de dependencias.
    // Spring buscará y creará instancias de las clases necesarias.
    @Autowired
    private WorkerServiceInterface workerService;
    @Autowired
    private TareaServiceInterface tareaService;
    @Autowired
    private UbicacionServiceInterface ubicacionService;
    @Autowired
    private TareaCumplidaServiceInterface tareaCumplidaService;
    @Autowired
    private TareaRepository tareaRepository;
    @Autowired
    private TareaCumplidaRepository tareaCumplidaRepository;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private UbicacionRepository ubicacionRepository;
    @Autowired
    private SociedadService sociedadService;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private MensajeService mensajeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UbicacionTareaService ubicacionTareaService;
    @Autowired
    private AdminService adminService;

    // Las anotaciones @GetMapping y @PostMapping definen qué métodos del controlador manejan qué rutas HTTP.
    @GetMapping("/adminsmenu")
    public String adminMenu(@PathVariable Long sociedadId, Model model, HttpServletRequest request) {
        // Este método maneja las solicitudes GET a la ruta /adminsmenu.
        request.getSession().setAttribute("sociedadId", sociedadId);
        List<Sociedad> sociedades = sociedadService.findAll();
        model.addAttribute("sociedades", sociedades);
        return "admins/adminsmenu"; // Devuelve la vista que debe ser renderizada.
    }
    // Este método maneja las solicitudes GET a la ruta /cambio-sociedad. Se verifica si el id de la sociedad en la ruta coincide con el almacenado en la sesión.
    // Si no coinciden, se redirige al usuario al menú de administrador. De lo contrario, se recogen todas las sociedades y se agregan al modelo para ser mostradas en la vista.
    @GetMapping("/cambio-sociedad")
    public String cambioSociedad(@PathVariable Long sociedadId, Model model, HttpServletRequest request) {
        // Verifica si el id de la sociedad en la ruta coincide con el almacenado en la sesión.
        if (!sociedadId.equals(request.getSession().getAttribute("sociedadId"))) {
            // Si no coincide, redirige al usuario al menú de administrador.
            return "redirect:/admin/{sociedadId}/adminsmenu";
        }
        // Recoge todas las sociedades
        List<Sociedad> sociedades = sociedadService.findAll();
        // Agrega las sociedades al modelo
        model.addAttribute("sociedades", sociedades);
        // Retorna el nombre de la vista a mostrar
        return "admins/cambio-sociedad";
    }

    // Este método maneja las solicitudes POST a la ruta /cambiar-sociedad. Se actualiza el id de la sociedad en la sesión y se redirige al usuario al menú de administrador.
    @PostMapping("/cambiar-sociedad")
    public String cambiarSociedad(@PathVariable Long sociedadId, @RequestParam Long nuevaSociedadId, HttpServletRequest request) {
        // Actualiza el id de la sociedad en la sesión
        request.getSession().setAttribute("sociedadId", nuevaSociedadId);
        // Redirige al usuario al menú de administrador
        return "redirect:/admin/{sociedadId}/adminsmenu";
    }

    // Este método maneja las solicitudes GET a la ruta /workers. Se recogen todos los trabajadores de una sociedad y se agregan al modelo para ser mostrados en la vista.
    @GetMapping("/workers")
    public String getAllWorkers(@PathVariable Long sociedadId, Model model) {
        // Recoge todos los trabajadores de una sociedad
        List<Worker> workers = workerService.getWorkersBySociedad(sociedadId);
        // Agrega los trabajadores al modelo
        model.addAttribute("workers", workers);
        // Retorna el nombre de la vista a mostrar
        return "admins/workers";
    }

    // Este método maneja las solicitudes GET a la ruta /crear-trabajador. Se crea un nuevo DTO de trabajador y se agrega al modelo para ser llenado en el formulario de la vista.
    @GetMapping("/crear-trabajador")
    public String showCreateWorkerForm(Model model) {
        // Crea un nuevo DTO de trabajador
        model.addAttribute("workerDTO", new WorkerDTO());
        // Retorna el nombre de la vista a mostrar
        return "admins/crear-trabajador";
    }

    // Este método maneja las solicitudes POST a la ruta /crear-trabajador. Se recoge la información del trabajador del formulario, se guarda el trabajador y se redirige al usuario a la lista de trabajadores.
    @PostMapping("/crear-trabajador")
    public String addWorker(@PathVariable("sociedadId") Long sociedadId, @ModelAttribute WorkerDTO workerDTO, RedirectAttributes redirectAttributes) {
        // Asigna el id de la sociedad al DTO del trabajador
        workerDTO.setSociedadId(sociedadId);
        // Guarda el trabajador
        workerService.saveWorker(workerDTO);
        // Agrega un mensaje flash para ser mostrado tras la redirección
        redirectAttributes.addFlashAttribute("message", "Trabajador creado exitosamente");
        // Redirige al usuario a la lista de trabajadores
        return "redirect:/admin/{sociedadId}/workers";
    }
    // Este método maneja las solicitudes GET a la ruta /workers/editar/{id}. Se busca un trabajador por su id, se crea un DTO de trabajador y se agrega al modelo para ser llenado en el formulario de la vista.
    @GetMapping("/workers/editar/{id}")
    public String showEditWorkerForm(@PathVariable Long id, Model model) {
        // Busca un trabajador por su id
        Worker worker = workerService.getWorkerById(id);
        // Crea un DTO de trabajador y copia los datos del trabajador
        WorkerDTO workerDTO = new WorkerDTO();
        workerDTO.setId(worker.getId());
        workerDTO.setName(worker.getName());
        workerDTO.setCargo(worker.getCargo());
        // Agrega el DTO al modelo
        model.addAttribute("workerDTO", workerDTO);
        // Retorna el nombre de la vista a mostrar
        return "admins/editar-worker";
    }

    // Este método maneja las solicitudes POST a la ruta /workers/{id}/update. Se recoge la información del trabajador del formulario, se actualiza el trabajador y se redirige al usuario a la lista de trabajadores.
    @PostMapping("/workers/{id}/update")
    public String updateWorker(@PathVariable Long sociedadId, @PathVariable Long id, @ModelAttribute WorkerDTO workerDTO, RedirectAttributes redirectAttributes) {
        // Actualiza el trabajador
        workerService.updateWorker(id, workerDTO);
        // Agrega un mensaje flash para ser mostrado tras la redirección
        redirectAttributes.addFlashAttribute("message", "Trabajador actualizado exitosamente");
        // Redirige al usuario a la lista de trabajadores
        return "redirect:/admin/{sociedadId}/workers";
    }

    // Este método maneja las solicitudes GET a la ruta /workers/{id}. Se busca un trabajador por su id y se agrega al modelo para ser mostrado en la vista.
    @GetMapping("/workers/{id}")
    public String getWorker(@PathVariable Long id, Model model) {
        // Busca un trabajador por su id
        Worker worker = workerService.getWorkerById(id);
        // Agrega el trabajador al modelo
        model.addAttribute("worker", worker);
        // Retorna el nombre de la vista a mostrar
        return "admins/workerDetails";
    }

    // Este método maneja las solicitudes GET a la ruta /workers/{id}/delete. Se borra un trabajador por su id y se redirige al usuario a la lista de trabajadores.
    @GetMapping("/workers/{id}/delete")
    public String deleteWorker(@PathVariable Long sociedadId, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Borra el rol del trabajador por su id de usuario
        workerService.deleteRoleByUserId(id);
        // Borra el trabajador por su id
        workerService.deleteWorkerById(id);
        // Agrega un mensaje flash para ser mostrado tras la redirección
        redirectAttributes.addFlashAttribute("message", "Trabajador eliminado exitosamente");
        // Redirige al usuario a la lista de trabajadores
        return "redirect:/admin/{sociedadId}/workers";
    }
    // Este método maneja las solicitudes GET a la ruta /cambiar-contrasena. No necesita agregar nada al modelo, simplemente devuelve el nombre de la vista para el formulario de cambio de contraseña.
    @GetMapping("/cambiar-contrasena")
    public String showChangePasswordForm(@PathVariable Long sociedadId, Model model) {
        return "admins/cambiar-contrasena";
    }

    // Este método maneja las solicitudes POST a la ruta /cambiar-contrasena. Recoge la nueva contraseña del formulario, la establece para el usuario actual y redirige al menú de administrador.
    @PostMapping("/cambiar-contrasena")
    public String changePasswordAdmin(@RequestParam String newPassword, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails currentUser) {
        // Cambia la contraseña del usuario actual
        adminService.changePasswordAdmin(currentUser, newPassword);
        // Agrega un mensaje flash para ser mostrado tras la redirección
        redirectAttributes.addFlashAttribute("message", "Contraseña actualizada exitosamente");
        // Redirige al menú de administrador
        return "redirect:/admin/{sociedadId}/adminsmenu";
    }

    // Este método maneja las solicitudes GET a la ruta /ubicaciones/asignar. Recoge todas las tareas distintas por nombre y las agrega al modelo junto con el id de la ubicación para ser mostradas en el formulario de la vista.
    @GetMapping("/ubicaciones/asignar")
    public String showAssignTareaForm(@RequestParam Long ubicacionId, Model model) {
        // Recoge todas las tareas distintas por nombre
        List<Tarea> tareas = tareaService.findAllDistinctByName();
        // Agrega las tareas y el id de la ubicación al modelo
        model.addAttribute("tareas", tareas);
        model.addAttribute("ubicacionId", ubicacionId);
        // Retorna el nombre de la vista a mostrar
        return "admins/asignar-tareas";
    }

    // Este método maneja las solicitudes POST a la ruta /ubicaciones/asignar. Recoge la información de la tarea del formulario, crea una nueva tarea si no existe una con el mismo id, la guarda, la asigna a la ubicación correspondiente y redirige a la lista de ubicaciones.
    @PostMapping("/ubicaciones/asignar")
    public String createAndAssignTarea(@RequestParam Long ubicacionId, @RequestParam Long tareaId, @RequestParam(required = false) String name, @RequestParam(required = false) String descripcion, RedirectAttributes redirectAttributes) {
        // Crea un DTO de tarea y copia los datos de la tarea original si existe
        Tarea tareaOriginal;
        TareaDTO tareaDTO = new TareaDTO();

        if (tareaId != 0) {
            tareaOriginal = tareaService.getTareaById(tareaId);
            tareaDTO.setName(tareaOriginal.getName());
            tareaDTO.setDescripcion(tareaOriginal.getDescripcion());
        } else {
            tareaDTO.setName(name);
            tareaDTO.setDescripcion(descripcion);
        }

        // Guarda la nueva tarea
        Tarea nuevaTarea = tareaService.saveTarea(tareaDTO);

        // Asigna la nueva tarea a la ubicación
        ubicacionService.addTareaAUbicacion(ubicacionId, tareaDTO);

        // Agrega un mensaje flash para ser mostrado tras la redirección
        redirectAttributes.addFlashAttribute("message", "Tarea asignada a la ubicación exitosamente");
        // Redirige a la lista de ubicaciones
        return "redirect:/admin/{sociedadId}/ubicaciones";
    }
    // Este método maneja las solicitudes GET a la ruta /tareas/editar/{id}. Recoge la tarea a editar por su id y la agrega al modelo para ser mostrada en el formulario de la vista.
    @GetMapping("/tareas/editar/{id}")
    public String showEditTareaForm(@PathVariable Long id, Model model) {
        // Recoge la tarea a editar por su id
        Tarea tarea = tareaService.getTareaById(id);
        // Crea un DTO de tarea y copia los datos de la tarea a editar
        TareaDTO tareaDTO = new TareaDTO();
        tareaDTO.setId(tarea.getId());
        tareaDTO.setName(tarea.getName());
        tareaDTO.setDescripcion(tarea.getDescripcion());
        // Agrega el DTO al modelo
        model.addAttribute("tareaDTO", tareaDTO);
        // Retorna el nombre de la vista a mostrar
        return "admins/editar-tarea";
    }

    // Este método maneja las solicitudes POST a la ruta /tareas/{id}/update. Recoge la información de la tarea del formulario y actualiza la tarea correspondiente en la base de datos.
    @PostMapping("/tareas/{id}/update")
    public String updateTarea(@PathVariable Long sociedadId, @PathVariable Long id, @ModelAttribute TareaDTO tareaDTO, RedirectAttributes redirectAttributes) {
        // Actualiza la tarea en la base de datos
        tareaService.updateTarea(id, tareaDTO);
        // Agrega un mensaje flash para ser mostrado tras la redirección
        redirectAttributes.addFlashAttribute("message", "Tarea actualizada exitosamente");
        // Redirige a la lista de ubicaciones
        return "redirect:/admin/{sociedadId}/ubicaciones";
    }

    // Este método maneja las solicitudes GET a la ruta /tareas/{id}/delete. Elimina la tarea correspondiente de la base de datos.
    @GetMapping("/tareas/{id}/delete")
    public String deleteTarea(@PathVariable Long sociedadId, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Elimina la tarea de la base de datos
        tareaService.deleteTareaById(id);
        // Agrega un mensaje flash para ser mostrado tras la redirección
        redirectAttributes.addFlashAttribute("message", "Tarea eliminada exitosamente");
        // Redirige a la lista de ubicaciones
        return "redirect:/admin/{sociedadId}/ubicaciones";
    }

    // Este método maneja las solicitudes GET a la ruta /tareas/{id}. Recoge la tarea por su id y la agrega al modelo para ser mostrada en la vista.
    @GetMapping("/tareas/{id}")
    public String getTarea(@PathVariable Long id, Model model) {
        // Recoge la tarea por su id
        Tarea tarea = tareaService.getTareaById(id);
        // Agrega la tarea al modelo
        model.addAttribute("tarea", tarea);
        // Retorna el nombre de la vista a mostrar
        return "admins/tareaDetails";
    }

    // Este método maneja las solicitudes GET a la ruta /ubicaciones. Recoge todas las ubicaciones de la sociedad correspondiente y las tareas asociadas a cada ubicación y las agrega al modelo para ser mostradas en la vista.
    @GetMapping("/ubicaciones")
    public String getAllUbicaciones(@PathVariable("sociedadId") Long sociedadId, Model model) {
        // Recoge la sociedad por su id
        Sociedad sociedad = sociedadService.findById(sociedadId)
                .orElseThrow(() -> new NotFoundException("Sociedad no encontrada con el ID: " + sociedadId));

        // Recoge todas las ubicaciones de la sociedad
        List<Ubicacion> ubicaciones = ubicacionService.findAllBySociedad(sociedad);
        // Agrega las ubicaciones al modelo
        model.addAttribute("ubicaciones", ubicaciones);

        // Recoge las tareas asociadas a cada ubicación y las agrupa por ubicación
        Map<Ubicacion, List<Pair<Long, Pair<String, String>>>> tareasAgrupadasPorUbicacion = ubicaciones.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        ubicacion -> ubicacionTareaService.getAllByUbicacionId(ubicacion.getId()).stream()
                                .map(ubicacionTarea -> {
                                    Tarea tarea = ubicacionTarea.getTarea();
                                    return Pair.of(tarea.getId(), Pair.of(tarea.getName(), tarea.getDescripcion()));
                                })
                                .sorted(Comparator.comparing(Pair::getKey)) // Ordena las tareas por tareaId
                                .collect(Collectors.toList()),
                        (u1, u2) -> u1, // En caso de conflicto, toma el primer valor
                        LinkedHashMap::new // Conserva el orden de las ubicaciones
                ));

        // Agrega las tareas agrupadas por ubicación al modelo
        model.addAttribute("tareasAgrupadasPorUbicacion", tareasAgrupadasPorUbicacion);

        // Retorna el nombre de la vista a mostrar
        return "admins/ubicaciones";
    }
    // Este método maneja las solicitudes POST a la ruta /ubicaciones. Recoge la información de la ubicación del formulario, la guarda en la base de datos y redirige a la lista de ubicaciones.
    @PostMapping("/ubicaciones")
    public String addUbicacion(@PathVariable("sociedadId") Long sociedadId, @ModelAttribute UbicacionDTO ubicacionDTO, RedirectAttributes redirectAttributes) {
        // Ajusta el id de la sociedad en el DTO de la ubicación
        ubicacionDTO.setSociedadId(sociedadId);
        // Guarda la nueva ubicación en la base de datos
        ubicacionService.save(ubicacionDTO);
        // Agrega un mensaje flash para ser mostrado tras la redirección
        redirectAttributes.addFlashAttribute("message", "Ubicación creada exitosamente");
        // Redirige a la lista de ubicaciones
        return "redirect:/admin/{sociedadId}/ubicaciones";
    }

    // Este método maneja las solicitudes POST a la ruta /ubicaciones/{id}/update. Recoge la información de la ubicación del formulario y actualiza la ubicación correspondiente en la base de datos.
    @PostMapping("/ubicaciones/{id}/update")
    public String updateUbicacion(@PathVariable Long sociedadId, @PathVariable Long id, @ModelAttribute UbicacionDTO ubicacionDTO, RedirectAttributes redirectAttributes) {
        // Actualiza la ubicación en la base de datos
        ubicacionService.updateUbicacion(id, ubicacionDTO);
        // Agrega un mensaje flash para ser mostrado tras la redirección
        redirectAttributes.addFlashAttribute("message", "Ubicación actualizada exitosamente");
        // Redirige a la lista de ubicaciones
        return "redirect:/admin/{sociedadId}/ubicaciones";
    }

    // Este método maneja las solicitudes GET a la ruta /ubicaciones/{id}. Recoge la ubicación por su id y la agrega al modelo para ser mostrada en la vista.
    @GetMapping("/ubicaciones/{id}")
    public String getUbicacion(@PathVariable Long id, Model model) {
        // Recoge la ubicación por su id
        Ubicacion ubicacion = ubicacionService.findById(id);
        // Agrega la ubicación al modelo
        model.addAttribute("ubicacion", ubicacion);
        // Retorna el nombre de la vista a mostrar
        return "admins/ubicacionDetails";
    }

    // Este método maneja las solicitudes GET a la ruta /ubicaciones/{id}/delete. Elimina la ubicación correspondiente de la base de datos.
    @GetMapping("/ubicaciones/{id}/delete")
    public String deleteUbicacion(@PathVariable Long sociedadId, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Elimina la ubicación de la base de datos
        ubicacionService.deleteById(id);
        // Agrega un mensaje flash para ser mostrado tras la redirección
        redirectAttributes.addFlashAttribute("message", "Ubicación eliminada exitosamente");
        // Redirige a la lista de ubicaciones
        return "redirect:/admin/{sociedadId}/ubicaciones";
    }

    // Este método maneja las solicitudes GET a la ruta /informes/diario/request. Retorna el nombre de la vista a mostrar.
    @GetMapping("/informes/diario/request")
    public String informeDiarioRequest(@PathVariable("sociedadId") Long sociedadId, Model model) {
        // Retorna el nombre de la vista a mostrar
        return "informes/informeDiarioRequest";
    }

    // Este método maneja las solicitudes POST a la ruta /informes/diario. Genera un informe diario con las tareas cumplidas y los trabajadores que las realizaron.
    @PostMapping("/informes/diario")
    public String informeDiario(@PathVariable("sociedadId") Long sociedadId, @RequestParam("fecha") String fechaStr, Model model) {
        // Recoge la sociedad por su id
        Sociedad sociedad = sociedadService.findById(sociedadId).orElseThrow(() -> new IllegalArgumentException("Sociedad no encontrada con ID: " + sociedadId));
        LocalDate fecha;

        // Intenta parsear la fecha proporcionada
        try {
            fecha = LocalDate.parse(fechaStr);
        } catch (DateTimeParseException e) {
            model.addAttribute("message", "La fecha proporcionada no es válida.");
            return "/error";
        }

        // Comprueba si la fecha proporcionada es una fecha futura
        if (fecha.isAfter(LocalDate.now())) {
            model.addAttribute("message", "La fecha proporcionada no puede ser una fecha futura.");
            return "/error";
        }

        // Formatea la fecha como una cadena de texto
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaString = fecha.format(formatter);

        // Añade la fecha formateada al modelo
        model.addAttribute("fechaString", fechaString);
        // Recoge todas las ubicaciones de la sociedad
        List<Ubicacion> ubicaciones = ubicacionService.findAllBySociedad(sociedad);

        // Inicializa los mapas que recogerán las tareas cumplidas y los trabajadores para cada turno y ubicación
        Map<Long, List<TareaCumplida>> tareasCumplidasMananaMap = new HashMap<>();
        Map<Long, List<TareaCumplida>> tareasCumplidasTardeMap = new HashMap<>();
        Map<Long, String> trabajadoresMananaMap = new HashMap<>();
        Map<Long, String> trabajadoresTardeMap = new HashMap<>();
        boolean hayTareas = false;

        // Itera sobre cada ubicación
        for (Ubicacion ubicacion : ubicaciones) {
            // Recoge las tareas cumplidas por ubicación, fecha y turno
            List<TareaCumplida> tareasCumplidasManana = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurno(ubicacion, fecha.atStartOfDay(), Turno.MANANA);
            List<TareaCumplida> tareasCumplidasTarde = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurno(ubicacion, fecha.atStartOfDay(), Turno.TARDE);

            // Comprueba si hay tareas cumplidas en alguno de los turnos
            if (!tareasCumplidasManana.isEmpty() || !tareasCumplidasTarde.isEmpty()) {
                hayTareas = true;
            }

            // Recoge los trabajadores que trabajaron en cada turno
            String trabajadoresManana = tareasCumplidasManana.stream()
                    .filter(tarea -> tarea.getTarea().getName().equals("¿Quien ha trabajado en este turno?"))
                    .findFirst()
                    .map(TareaCumplida::getComentario)
                    .orElse("Nadie trabajó aquí");

            String trabajadoresTarde = tareasCumplidasTarde.stream()
                    .filter(tarea -> tarea.getTarea().getName().equals("¿Quien ha trabajado en este turno?"))
                    .findFirst()
                    .map(TareaCumplida::getComentario)
                    .orElse("Nadie trabajó aquí");

            // Añade los trabajadores a los mapas correspondientes
            trabajadoresMananaMap.put(ubicacion.getId(), trabajadoresManana);
            trabajadoresTardeMap.put(ubicacion.getId(), trabajadoresTarde);

            // Filtra las listas de tareas cumplidas para excluir la tarea de "¿Quien ha trabajado en este turno?"
            tareasCumplidasManana = tareasCumplidasManana.stream()
                    .filter(tarea -> !tarea.getTarea().getName().equals("¿Quien ha trabajado en este turno?"))
                    .collect(Collectors.toList());

            tareasCumplidasTarde = tareasCumplidasTarde.stream()
                    .filter(tarea -> !tarea.getTarea().getName().equals("¿Quien ha trabajado en este turno?"))
                    .collect(Collectors.toList());

            // Añade las tareas cumplidas a los mapas correspondientes
            tareasCumplidasMananaMap.put(ubicacion.getId(), tareasCumplidasManana);
            tareasCumplidasTardeMap.put(ubicacion.getId(), tareasCumplidasTarde);
        }

        // Añade los mapas y la fecha al modelo
        model.addAttribute("ubicaciones", ubicaciones);
        model.addAttribute("tareasCumplidasMananaMap", tareasCumplidasMananaMap);
        model.addAttribute("tareasCumplidasTardeMap", tareasCumplidasTardeMap);
        model.addAttribute("trabajadoresMananaMap", trabajadoresMananaMap);
        model.addAttribute("trabajadoresTardeMap", trabajadoresTardeMap);
        model.addAttribute("fecha", fecha);

        // Si no hay tareas, añade un mensaje al modelo
        if (!hayTareas) {
            model.addAttribute("message", "No se encontraron tareas cumplidas para la fecha proporcionada.");
        }

        // Retorna el nombre de la vista a mostrar
        return "informes/informeDiario";
    }
    // Este método maneja las solicitudes POST a la ruta /informes/diario/export. Exporta el informe diario a un archivo Excel.
    @PostMapping("/informes/diario/export")
    public void exportarInformeDiarioExcel(@RequestParam("fecha") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha, HttpServletResponse response, HttpSession session) throws IOException {
        // Recoge la sociedad seleccionada de la sesión
        String sociedadSeleccionadaId = session.getAttribute("sociedadSeleccionadaId") != null ? session.getAttribute("sociedadSeleccionadaId").toString() : "1"; // O cualquier otro valor predeterminado que desees utilizar
        // Recoge la sociedad por su id
        Sociedad sociedad = sociedadService.findById(Long.valueOf(sociedadSeleccionadaId)).orElseThrow(() -> new IllegalArgumentException("Sociedad no encontrada con ID: " + sociedadSeleccionadaId));

        // Configura la respuesta para que sea descargada como un archivo Excel
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Informe_Diario_Tareas" + fecha + ".xlsx");

        try (Workbook workbook = new XSSFWorkbook()) {
            // Recoge todas las ubicaciones de la sociedad
            List<Ubicacion> ubicaciones = ubicacionService.findAllBySociedad(sociedad);

            // Inicializa los mapas que recogerán las tareas cumplidas para cada turno y ubicación
            Map<Long, List<TareaCumplida>> tareasCumplidasMananaMap = new HashMap<>();
            Map<Long, List<TareaCumplida>> tareasCumplidasTardeMap = new HashMap<>();

            // Itera sobre cada ubicación
            for (Ubicacion ubicacion : ubicaciones) {
                // Recoge las tareas cumplidas por ubicación, fecha y turno
                List<TareaCumplida> tareasCumplidasManana = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurno(ubicacion, fecha.atStartOfDay(), Turno.MANANA);
                List<TareaCumplida> tareasCumplidasTarde = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurno(ubicacion, fecha.atStartOfDay(), Turno.TARDE);

                // Añade las tareas cumplidas a los mapas correspondientes
                tareasCumplidasMananaMap.put(ubicacion.getId(), tareasCumplidasManana);
                tareasCumplidasTardeMap.put(ubicacion.getId(), tareasCumplidasTarde);

                // Crea una hoja de cálculo para cada ubicación
                Sheet sheet = workbook.createSheet(cleanSheetName(ubicacion.getName()));

                // Llena la hoja de cálculo con los datos correspondientes
                fillSheetWithData(sheet, workbook, ubicacion, tareasCumplidasManana, tareasCumplidasTarde, sociedadSeleccionadaId);
            }

            // Escribe el libro de trabajo en la respuesta
            workbook.write(response.getOutputStream());
        }
    }
    // Este método llena una hoja de cálculo con los datos de las tareas cumplidas en una ubicación para los turnos de mañana y tarde
    private void fillSheetWithData(Sheet sheet, Workbook workbook, Ubicacion ubicacion, List<TareaCumplida> tareasCumplidasManana, List<TareaCumplida> tareasCumplidasTarde, String sociedadId) {

        // Establece el estilo para los títulos de los turnos "Mañana" y "Tarde"
        CellStyle turnTitleStyle = workbook.createCellStyle();
        Font turnTitleFont = workbook.createFont();
        turnTitleFont.setBold(true);
        turnTitleFont.setColor(IndexedColors.WHITE.getIndex());
        turnTitleFont.setFontHeightInPoints((short) 14);
        turnTitleStyle.setFont(turnTitleFont);
        turnTitleStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        turnTitleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Establece el estilo para el título de la ubicación
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 18);
        titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());
        titleStyle.setFont(titleFont);

        // Recoge los nombres de los trabajadores que trabajaron en cada turno
        String trabajadoresManana = tareasCumplidasManana.stream()
                .filter(tarea -> tarea.getTarea().getName().equals("¿Quien ha trabajado en este turno?"))
                .findFirst()
                .map(TareaCumplida::getComentario)
                .orElse("Nadie trabajó aquí");

        String trabajadoresTarde = tareasCumplidasTarde.stream()
                .filter(tarea -> tarea.getTarea().getName().equals("¿Quien ha trabajado en este turno?"))
                .findFirst()
                .map(TareaCumplida::getComentario)
                .orElse("Nadie trabajó aquí");

        // Filtra las listas de tareas cumplidas para excluir la tarea de "¿Quien ha trabajado en este turno?"
        tareasCumplidasManana = tareasCumplidasManana.stream()
                .filter(tarea -> !tarea.getTarea().getName().equals("¿Quien ha trabajado en este turno?"))
                .collect(Collectors.toList());

        tareasCumplidasTarde = tareasCumplidasTarde.stream()
                .filter(tarea -> !tarea.getTarea().getName().equals("¿Quien ha trabajado en este turno?"))
                .collect(Collectors.toList());

        // Escribe el nombre de la ubicación en la hoja de cálculo
        Row ubicacionRow = sheet.createRow(0);
        ubicacionRow.setHeightInPoints(25);
        Cell ubicacionCell = ubicacionRow.createCell(0);
        ubicacionCell.setCellValue(ubicacion.getName());
        ubicacionCell.setCellStyle(titleStyle);

        // Escribe el título del turno de mañana y la lista de trabajadores
        Row titleRow = sheet.createRow(1);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(turnTitleStyle);
        titleCell.setCellValue("Mañana");

        Cell trabajadoresCell = titleRow.createCell(1);
        trabajadoresCell.setCellValue(trabajadoresManana);

        // Escribe las cabeceras de la tabla para el turno de mañana
        Row headerRow = sheet.createRow(2);
        writeTableHeaders(sheet, headerRow);

        // Escribe las filas de datos para el turno de mañana
        int rowIndex = 3;
        for (TareaCumplida tareaCumplida : tareasCumplidasManana) {
            Row row = sheet.createRow(rowIndex++);
            writeTareaCumplidaData(workbook, row, tareaCumplida);
        }

        // Deja un espacio entre las tablas
        rowIndex++;

        // Escribe el título del turno de tarde y la lista de trabajadores
        titleRow = sheet.createRow(rowIndex++);
        titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(turnTitleStyle);
        titleCell.setCellValue("Tarde");

        trabajadoresCell = titleRow.createCell(1);
        trabajadoresCell.setCellValue(trabajadoresTarde);

        // Escribe las cabeceras de la tabla para el turno de tarde
        headerRow = sheet.createRow(rowIndex++);
        writeTableHeaders(sheet, headerRow);

        // Escribe las filas de datos para el turno de tarde
        for (TareaCumplida tareaCumplida : tareasCumplidasTarde) {
            Row row = sheet.createRow(rowIndex++);
            writeTareaCumplidaData(workbook, row, tareaCumplida);
        }
    }
    // Este método escribe los encabezados de las tablas de tareas cumplidas en una hoja de cálculo
    private void writeTableHeaders(Sheet sheet, Row headerRow) {
        String[] headers = {"Nombre", "Descripción", "Hecha", "Trabajador", "Comentario"};

        // Crea el estilo de las celdas de encabezado
        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        Font headerFont = sheet.getWorkbook().createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Define el ancho de las columnas
        sheet.setColumnWidth(0, 25 * 256); // A
        sheet.setColumnWidth(1, 35 * 256); // B
        sheet.setColumnWidth(3, 25 * 256); // D
        sheet.setColumnWidth(4, 35 * 256); // E

        // Escribe los encabezados en las celdas
        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(headerStyle);  // Aplica el estilo a las celdas de encabezado
        }
    }

    // Este método escribe los datos de una tarea cumplida en una fila de una hoja de cálculo
    private void writeTareaCumplidaData(Workbook workbook, Row row, TareaCumplida tareaCumplida) {
        // Crea el estilo para las columnas "Nombre", "Hecha" y "Trabajador"
        CellStyle boldStyle = workbook.createCellStyle();
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldStyle.setFont(boldFont);

        // Crea el estilo para centrar y poner en negrita el contenido de la celda
        CellStyle centerBoldStyle = workbook.createCellStyle();
        centerBoldStyle.setFont(boldFont);
        centerBoldStyle.setAlignment(HorizontalAlignment.CENTER);
        centerBoldStyle.setVerticalAlignment(VerticalAlignment.CENTER); // Agrega alineación vertical al estilo

        // Escribe el nombre de la tarea
        Cell cell = row.createCell(0);
        cell.setCellValue(tareaCumplida.getTarea().getName());
        cell.setCellStyle(centerBoldStyle);

        // Escribe la descripción de la tarea
        cell = row.createCell(1);
        cell.setCellValue(tareaCumplida.getTarea().getDescripcion());

        // Crea el estilo para ajustar el texto a la celda
        CellStyle wrapStyle = workbook.createCellStyle();
        wrapStyle.setWrapText(true);
        cell.setCellStyle(wrapStyle);

        // Escribe si la tarea fue cumplida
        cell = row.createCell(2);
        cell.setCellValue(tareaCumplida.isCumplida() ? "SI" : "NO");
        cell.setCellStyle(centerBoldStyle);

        // Escribe el nombre del trabajador que cumplió la tarea
        cell = row.createCell(3);
        cell.setCellValue(tareaCumplida.isCumplida() ? tareaCumplida.getWorker().getName() : "");
        cell.setCellStyle(centerBoldStyle);

        // Escribe el comentario de la tarea cumplida
        cell = row.createCell(4);
        cell.setCellValue(tareaCumplida.getComentario());
    }
    // Este método limpia el nombre de la hoja para asegurarse de que sea válido.
    // Algunos caracteres no son válidos en los nombres de las hojas en Excel.
    private String cleanSheetName(String name) {
        return name.replaceAll("[:\\\\/?*\\[\\]]", "_");
    }

    // Este método valida que el ID de la sociedad exista. Si no existe, lanza una excepción.
    private void validateSociedadId(Long sociedadId) {
        if (!sociedadService.existsById(sociedadId)) {
            throw new NotFoundException("Sociedad no encontrada con el ID: " + sociedadId);
        }
    }

    // Este método añade el logo de la sociedad a la hoja de cálculo.
    // Dependiendo del ID de la sociedad, se selecciona una imagen distinta.
    // La imagen se añade en la esquina superior izquierda de la hoja de cálculo.
    /*
    private void addLogoToSheet(Sheet sheet, Workbook workbook, String sociedadId) {
        InputStream logoInputStream = null;
        try {
            String imagePath = "";
            switch (sociedadId) {
                case "1":
                    imagePath = "/images/GOODPOLISH.png";
                    break;
                case "2":
                    imagePath = "/images/LAPUENTE.png";
                    break;
                case "3":
                    imagePath = "/images/SPB.png";
                    break;
                case "4":
                    imagePath = "/images/ISOTUBI.png";
                    break;
                default:
                    imagePath = "/images/GL_horizontal.png";
                    break;
            }
            logoInputStream = servletContext.getResourceAsStream(imagePath);
            byte[] imageBytes = IOUtils.toByteArray(logoInputStream);
            int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);

            CreationHelper helper = workbook.getCreationHelper();
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(0);
            anchor.setRow1(0);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (logoInputStream != null) {
                try {
                    logoInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
     */

    // Métodos de mensajes
    @GetMapping("/tablonanuncios")
    public String showTablonAnuncios(@PathVariable Long sociedadId, Model model, HttpServletRequest request) {
        List<Mensaje> mensajes = mensajeService.findMensajesCirculares().stream().filter(Mensaje::isActivo).collect(Collectors.toList());
        List<Worker> workers = workerRepository.findBySociedadId(sociedadId);
        model.addAttribute("mensajes", mensajes);
        model.addAttribute("workers", workers);
        return "admins/tablonanuncios";
    }

    @GetMapping("/crearmensaje")
    public String showCrearMensaje(@PathVariable Long sociedadId, Model model, HttpServletRequest request) {
        MensajeDTO mensajeDTO = new MensajeDTO();
        model.addAttribute("mensajeDTO", mensajeDTO);
        return "admins/crearmensaje";
    }

    @PostMapping("/enviarmensaje")
    public String sendMensaje(@PathVariable Long sociedadId, @ModelAttribute MensajeDTO mensajeDTO, RedirectAttributes redirectAttributes, Authentication authentication) {
        System.out.println("Método sendMensaje ejecutado");

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User emisor = userRepository.findByName(userDetails.getUsername()).orElseThrow(() -> new NotFoundException("Usuario no encontrado con el nombre: " + userDetails.getUsername()));

        if (mensajeDTO.isCircular()) {
            Mensaje mensaje = new Mensaje();
            mensaje.setEmisor(emisor);
            mensaje.setCircular(true);
            mensaje.setReceptor(null);
            mensaje.setActivo(mensajeDTO.isActivo());
            mensaje.setAsunto(mensajeDTO.getAsunto());
            mensaje.setContenido(mensajeDTO.getContenido());
            mensajeService.createMensaje(mensaje);
        } else {
            List<Long> receptorIds = mensajeDTO.getReceptorIds();
            for (Long receptorId : receptorIds) {
                Worker receptor = workerRepository.findById(receptorId).orElseThrow(() -> new NotFoundException("Usuario no encontrado con el ID: " + receptorId));
                Mensaje mensaje = new Mensaje();
                mensaje.setEmisor(emisor);
                mensaje.setReceptor(receptor);
                mensaje.setCircular(false);
                mensaje.setAsunto(mensajeDTO.getAsunto());
                mensaje.setContenido(mensajeDTO.getContenido());
                System.out.println("Receptor IDs: " + mensajeDTO.getReceptorIds());
                mensajeService.createMensaje(mensaje);
                System.out.println("Mensaje guardado: " + mensaje);
            }
        }

        redirectAttributes.addFlashAttribute("success", "Mensaje enviado con éxito");
        return "redirect:/admin/{sociedadId}/tablonanuncios";
    }


    @GetMapping("/mensajes/{id}")
    public String showMensaje(@PathVariable Long sociedadId, @PathVariable Long id, Model model, HttpServletRequest request) {
        Optional<Mensaje> optionalMensaje = mensajeService.findMensajeById(id);
        if (optionalMensaje.isPresent()) {
            Mensaje mensaje = optionalMensaje.get();
            model.addAttribute("mensaje", mensaje);
            return "admins/mensaje";
        } else {
            // Lógica cuando no se encuentra el mensaje
            return "redirect:/admin/{sociedadId}/tablonanuncios?error=El mensaje no se encontró";
        }
    }

    @DeleteMapping("/mensajes/{id}")
    public String deleteMensaje(@PathVariable Long sociedadId, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        mensajeService.deleteMensaje(id);
        redirectAttributes.addFlashAttribute("success", "Mensaje eliminado con éxito");
        return "redirect:/admin/{sociedadId}/tablonanuncios";
    }

    @PostMapping("/mensajes/{id}/toggleactivo")
    public String toggleMensajeActivo(@PathVariable Long sociedadId, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Mensaje> optionalMensaje = mensajeService.findMensajeById(id);
        if (optionalMensaje.isPresent()) {
            Mensaje mensaje = optionalMensaje.get();
            if (mensaje.isCircular()) {
                mensaje.setActivo(!mensaje.isActivo());
                mensajeService.createMensaje(mensaje);
                redirectAttributes.addFlashAttribute("success", "Estado del mensaje actualizado con éxito");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se puede cambiar el estado de un mensaje no circular");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Mensaje no encontrado");
        }
        return "redirect:/admin/{sociedadId}/tablonanuncios";
    }

}

