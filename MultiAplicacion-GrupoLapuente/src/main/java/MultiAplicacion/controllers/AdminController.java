package MultiAplicacion.controllers;


import MultiAplicacion.DTOs.*;
import MultiAplicacion.ENUMs.Turno;
import MultiAplicacion.controllers.interfaces.AdminControllerInterface;
import MultiAplicacion.entities.*;
import MultiAplicacion.repositories.*;
import MultiAplicacion.services.InformeService;
import MultiAplicacion.services.SociedadService;
import MultiAplicacion.services.interfaces.TareaCumplidaServiceInterface;
import MultiAplicacion.services.interfaces.TareaServiceInterface;
import MultiAplicacion.services.interfaces.UbicacionServiceInterface;
import MultiAplicacion.services.interfaces.WorkerServiceInterface;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.webjars.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/{sociedadId}")
public class AdminController implements AdminControllerInterface {

    @Autowired
    private WorkerServiceInterface workerService;

    @Autowired
    private TareaServiceInterface tareaService;

    @Autowired
    private UbicacionServiceInterface ubicacionService;

    @Autowired
    private TareaCumplidaServiceInterface tareaCumplidaService;

    @Autowired
    private InformeService informeService;
    @Autowired
    private TareaRepository tareaRepository;
    @Autowired
    private TareaCumplidaRepository tareaCumplidaRepository;
    @Autowired
    private InformeRepository informeRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;
    @Autowired
    private SociedadService sociedadService;

    @GetMapping("/adminsmenu")
    public String adminMenu(@PathVariable Long sociedadId, Model model, HttpServletRequest request) {
        request.getSession().setAttribute("sociedadId", sociedadId);
        List<Sociedad> sociedades = sociedadService.findAll();
        model.addAttribute("sociedades", sociedades);
        return "admins/adminsmenu";
    }

    @GetMapping("/cambio-sociedad")
    public String cambioSociedad(@PathVariable Long sociedadId, Model model, HttpServletRequest request) {
        if (!sociedadId.equals(request.getSession().getAttribute("sociedadId"))) {
            // Aquí puedes redirigir al usuario a una página de error o al menú principal
            return "redirect:/admin/{sociedadId}/adminsmenu";
        }
        List<Sociedad> sociedades = sociedadService.findAll();
        model.addAttribute("sociedades", sociedades);
        return "admins/cambio-sociedad";
    }

    @PostMapping("/cambiar-sociedad")
    public String cambiarSociedad(@PathVariable Long sociedadId, @RequestParam Long nuevaSociedadId, HttpServletRequest request) {
        request.getSession().setAttribute("sociedadId", nuevaSociedadId);
        return "redirect:/admin/{sociedadId}/adminsmenu";
    }


    @GetMapping("/workers")
    public String getAllWorkers(@PathVariable Long sociedadId, Model model) {
        List<Worker> workers = workerService.getWorkersBySociedad(sociedadId);
        model.addAttribute("workers", workers);
        return "admins/workers";
    }
    @GetMapping("/crear-trabajador")
    public String showCreateWorkerForm(Model model) {
        model.addAttribute("workerDTO", new WorkerDTO());
        return "admins/crear-trabajador";
    }
    @PostMapping("/crear-trabajador")
    public String addWorker(@PathVariable("sociedadId") Long sociedadId, @ModelAttribute WorkerDTO workerDTO, RedirectAttributes redirectAttributes) {
        workerDTO.setSociedadId(sociedadId);
        workerService.saveWorker(workerDTO);
        redirectAttributes.addFlashAttribute("message", "Trabajador creado exitosamente");
        return "redirect:/admin/{sociedadId}/workers";
    }

    @GetMapping("/workers/editar/{id}")
    public String showEditWorkerForm(@PathVariable Long id, Model model) {
        Worker worker = workerService.getWorkerById(id);
        WorkerDTO workerDTO = new WorkerDTO();
        workerDTO.setId(worker.getId());
        workerDTO.setName(worker.getName());
        workerDTO.setCargo(worker.getCargo());
        model.addAttribute("workerDTO", workerDTO);
        return "admins/editar-worker";
    }

    @PostMapping("/workers/{id}/update")
    public String updateWorker(@PathVariable Long sociedadId, @PathVariable Long id, @ModelAttribute WorkerDTO workerDTO, RedirectAttributes redirectAttributes) {
        workerService.updateWorker(id, workerDTO);
        redirectAttributes.addFlashAttribute("message", "Trabajador actualizado exitosamente");
        return "redirect:/admin/{sociedadId}/workers";
    }

    // Para ver los detalles de un trabajador
    @GetMapping("/workers/{id}")
    public String getWorker(@PathVariable Long id, Model model) {
        Worker worker = workerService.getWorkerById(id);
        model.addAttribute("worker", worker);
        return "admins/workerDetails";
    }

    @GetMapping("/workers/{id}/delete")
    public String deleteWorker(@PathVariable Long sociedadId, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        workerService.deleteWorkerById(id);
        redirectAttributes.addFlashAttribute("message", "Trabajador eliminado exitosamente");
        return "redirect:/admin/{sociedadId}/workers";
    }

    @GetMapping("/ubicaciones/asignar")
    public String showAssignTareaForm(@RequestParam Long ubicacionId, Model model) {
        List<Tarea> tareas = tareaService.findAllDistinctByName();
        model.addAttribute("tareas", tareas);
        model.addAttribute("ubicacionId", ubicacionId);
        return "admins/asignar-tareas";
    }

    @PostMapping("/ubicaciones/asignar")
    public String createAndAssignTarea(@RequestParam Long ubicacionId, @RequestParam Long tareaId, @RequestParam(required = false) String name, @RequestParam(required = false) String descripcion, RedirectAttributes redirectAttributes) {
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

        redirectAttributes.addFlashAttribute("message", "Tarea asignada a la ubicación exitosamente");
        return "redirect:/admin/{sociedadId}/ubicaciones";
    }

    @GetMapping("/tareas/editar/{id}")
    public String showEditTareaForm(@PathVariable Long id, Model model) {
        Tarea tarea = tareaService.getTareaById(id);
        TareaDTO tareaDTO = new TareaDTO();
        tareaDTO.setId(tarea.getId());
        tareaDTO.setName(tarea.getName());
        tareaDTO.setDescripcion(tarea.getDescripcion());
        model.addAttribute("tareaDTO", tareaDTO);
        return "admins/editar-tarea";
    }

    @PostMapping("/tareas/{id}/update")
    public String updateTarea(@PathVariable Long sociedadId, @PathVariable Long id, @ModelAttribute TareaDTO tareaDTO, RedirectAttributes redirectAttributes) {
        tareaService.updateTarea(id, tareaDTO);
        redirectAttributes.addFlashAttribute("message", "Tarea actualizada exitosamente");
        return "redirect:/admin/{sociedadId}/ubicaciones";
    }

    @GetMapping("/tareas/{id}/delete")
    public String deleteTarea(@PathVariable Long sociedadId, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        tareaService.deleteTareaById(id);
        redirectAttributes.addFlashAttribute("message", "Tarea eliminada exitosamente");
        return "redirect:/admin/{sociedadId}/tareas";
    }

    // Para ver los detalles de una tarea
    @GetMapping("/tareas/{id}")
    public String getTarea(@PathVariable Long id, Model model) {
        Tarea tarea = tareaService.getTareaById(id);
        model.addAttribute("tarea", tarea);
        return "admins/tareaDetails";
    }

    @GetMapping("/ubicaciones")
    public String getAllUbicaciones(@PathVariable("sociedadId") Long sociedadId, Model model) {
        Sociedad sociedad = sociedadService.findById(sociedadId)
                .orElseThrow(() -> new NotFoundException("Sociedad no encontrada con el ID: " + sociedadId));

        List<Ubicacion> ubicaciones = ubicacionService.findAllBySociedadOrderedById(sociedad);
        model.addAttribute("ubicaciones", ubicaciones);

        Map<Ubicacion, List<Pair<Long, Pair<String, String>>>> tareasAgrupadasPorUbicacion = ubicaciones.stream()
                .collect(Collectors.toMap(
                        ubicacion -> ubicacion,
                        ubicacion -> ubicacion.getTareas().stream()
                                .sorted(Comparator.comparing(Tarea::getId)) // Ordena las tareas por tareaId
                                .map(tarea -> Pair.of(tarea.getId(), Pair.of(tarea.getName(), tarea.getDescripcion())))
                                .collect(Collectors.toList()),
                        (u1, u2) -> u1, // En caso de conflicto, toma el primer valor
                        LinkedHashMap::new // Conserva el orden de las ubicaciones
                ));

        model.addAttribute("tareasAgrupadasPorUbicacion", tareasAgrupadasPorUbicacion);

        return "admins/ubicaciones";
    }


    @PostMapping("/ubicaciones")
    public String addUbicacion(@PathVariable("sociedadId") Long sociedadId, @ModelAttribute UbicacionDTO ubicacionDTO, RedirectAttributes redirectAttributes) {
        ubicacionDTO.setSociedadId(sociedadId);
        ubicacionService.save(ubicacionDTO);
        redirectAttributes.addFlashAttribute("message", "Ubicación creada exitosamente");
        return "redirect:/admin/{sociedadId}/ubicaciones";
    }

    @PostMapping("/ubicaciones/{id}/update")
    public String updateUbicacion(@PathVariable Long sociedadId, @PathVariable Long id, @ModelAttribute UbicacionDTO ubicacionDTO, RedirectAttributes redirectAttributes) {
        ubicacionService.updateUbicacion(id, ubicacionDTO);
        redirectAttributes.addFlashAttribute("message", "Ubicación actualizada exitosamente");
        return "redirect:/admin/{sociedadId}/ubicaciones";
    }

    // Para ver los detalles de una ubicación
    @GetMapping("/ubicaciones/{id}")
    public String getUbicacion(@PathVariable Long id, Model model) {
        Ubicacion ubicacion = ubicacionService.findById(id);
        model.addAttribute("ubicacion", ubicacion);
        return "admins/ubicacionDetails";
    }

    @GetMapping("/ubicaciones/{id}/delete")
    public String deleteUbicacion(@PathVariable Long sociedadId, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        ubicacionService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Ubicación eliminada exitosamente");
        return "redirect:/admin/{sociedadId}/ubicaciones";
    }

    @GetMapping("/informes/diario/request")
    public String informeDiarioRequest(@PathVariable("sociedadId") Long sociedadId, Model model) {
        return "informes/informeDiarioRequest";
    }

    @PostMapping("/informes/diario")
    public String informeDiario(@RequestParam("fecha") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha, Model model) {
        List<Ubicacion> ubicaciones = ubicacionService.findAll();
        Map<Long, List<TareaCumplida>> tareasCumplidasMananaMap = new HashMap<>();
        Map<Long, List<TareaCumplida>> tareasCumplidasTardeMap = new HashMap<>();

        for (Ubicacion ubicacion : ubicaciones) {
            List<TareaCumplida> tareasCumplidasManana = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurno(ubicacion, fecha.atStartOfDay(), Turno.MANANA);
            List<TareaCumplida> tareasCumplidasTarde = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurno(ubicacion, fecha.atStartOfDay(), Turno.TARDE);

            tareasCumplidasMananaMap.put(ubicacion.getId(), tareasCumplidasManana);
            tareasCumplidasTardeMap.put(ubicacion.getId(), tareasCumplidasTarde);
        }

        model.addAttribute("ubicaciones", ubicaciones);
        model.addAttribute("tareasCumplidasMananaMap", tareasCumplidasMananaMap);
        model.addAttribute("tareasCumplidasTardeMap", tareasCumplidasTardeMap);
        model.addAttribute("fecha", fecha);

        return "informes/informeDiario";
    }

}

