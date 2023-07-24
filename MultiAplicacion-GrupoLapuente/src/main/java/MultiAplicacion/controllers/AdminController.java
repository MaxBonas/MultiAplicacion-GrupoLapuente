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

    // AdminController.java
    @GetMapping("/workers/{id}/delete")
    public String deleteWorker(@PathVariable Long sociedadId, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        workerService.deleteRoleByUserId(id);
        workerService.deleteWorkerById(id);
        redirectAttributes.addFlashAttribute("message", "Trabajador eliminado exitosamente");
        return "redirect:/admin/{sociedadId}/workers";
    }
    @GetMapping("/cambiar-contrasena")
    public String showChangePasswordForm(@PathVariable Long sociedadId, Model model) {
        return "admins/cambiar-contrasena";
    }
    @PostMapping("/cambiar-contrasena")
    public String changePasswordAdmin(@RequestParam String newPassword, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails currentUser) {
        adminService.changePasswordAdmin(currentUser, newPassword);
        redirectAttributes.addFlashAttribute("message", "Contraseña actualizada exitosamente");
        return "redirect:/admin/{sociedadId}/adminsmenu";
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
        return "redirect:/admin/{sociedadId}/ubicaciones";
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

        List<Ubicacion> ubicaciones = ubicacionService.findAllBySociedad(sociedad);
        model.addAttribute("ubicaciones", ubicaciones);

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
    public String informeDiario(@PathVariable("sociedadId") Long sociedadId, @RequestParam("fecha") String fechaStr, Model model) {
        Sociedad sociedad = sociedadService.findById(sociedadId).orElseThrow(() -> new IllegalArgumentException("Sociedad no encontrada con ID: " + sociedadId));
        LocalDate fecha;

        try {
            fecha = LocalDate.parse(fechaStr);
        } catch (DateTimeParseException e) {
            model.addAttribute("message", "La fecha proporcionada no es válida.");
            return "/error";
        }

        if (fecha.isAfter(LocalDate.now())) {
            model.addAttribute("message", "La fecha proporcionada no puede ser una fecha futura.");
            return "/error";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaString = fecha.format(formatter);

        model.addAttribute("fechaString", fechaString);
        List<Ubicacion> ubicaciones = ubicacionService.findAllBySociedad(sociedad);
        Map<Long, List<TareaCumplida>> tareasCumplidasMananaMap = new HashMap<>();
        Map<Long, List<TareaCumplida>> tareasCumplidasTardeMap = new HashMap<>();
        Map<Long, String> trabajadoresMananaMap = new HashMap<>();
        Map<Long, String> trabajadoresTardeMap = new HashMap<>();
        boolean hayTareas = false;

        for (Ubicacion ubicacion : ubicaciones) {
            List<TareaCumplida> tareasCumplidasManana = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurno(ubicacion, fecha.atStartOfDay(), Turno.MANANA);
            List<TareaCumplida> tareasCumplidasTarde = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurno(ubicacion, fecha.atStartOfDay(), Turno.TARDE);

            if (!tareasCumplidasManana.isEmpty() || !tareasCumplidasTarde.isEmpty()) {
                hayTareas = true;
            }

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

            trabajadoresMananaMap.put(ubicacion.getId(), trabajadoresManana);
            trabajadoresTardeMap.put(ubicacion.getId(), trabajadoresTarde);

            // Filtrar las listas para excluir la tarea de "¿Quien ha trabajado en este turno?"
            tareasCumplidasManana = tareasCumplidasManana.stream()
                    .filter(tarea -> !tarea.getTarea().getName().equals("¿Quien ha trabajado en este turno?"))
                    .collect(Collectors.toList());

            tareasCumplidasTarde = tareasCumplidasTarde.stream()
                    .filter(tarea -> !tarea.getTarea().getName().equals("¿Quien ha trabajado en este turno?"))
                    .collect(Collectors.toList());

            tareasCumplidasMananaMap.put(ubicacion.getId(), tareasCumplidasManana);
            tareasCumplidasTardeMap.put(ubicacion.getId(), tareasCumplidasTarde);
        }

        model.addAttribute("ubicaciones", ubicaciones);
        model.addAttribute("tareasCumplidasMananaMap", tareasCumplidasMananaMap);
        model.addAttribute("tareasCumplidasTardeMap", tareasCumplidasTardeMap);
        model.addAttribute("trabajadoresMananaMap", trabajadoresMananaMap);
        model.addAttribute("trabajadoresTardeMap", trabajadoresTardeMap);
        model.addAttribute("fecha", fecha);

        if (!hayTareas) {
            model.addAttribute("message", "No se encontraron tareas cumplidas para la fecha proporcionada.");
        }

        return "informes/informeDiario";
    }

    @PostMapping("/informes/diario/export")
    public void exportarInformeDiarioExcel(@RequestParam("fecha") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha, HttpServletResponse response, HttpSession session) throws IOException {
        String sociedadSeleccionadaId = session.getAttribute("sociedadSeleccionadaId") != null ? session.getAttribute("sociedadSeleccionadaId").toString() : "1"; // O cualquier otro valor predeterminado que desees utilizar
        Sociedad sociedad = sociedadService.findById(Long.valueOf(sociedadSeleccionadaId)).orElseThrow(() -> new IllegalArgumentException("Sociedad no encontrada con ID: " + sociedadSeleccionadaId));
        System.out.println("Fecha recibida: " + fecha);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Informe_Diario_Tareas" + fecha + ".xlsx");

        try (Workbook workbook = new XSSFWorkbook()) {
            List<Ubicacion> ubicaciones = ubicacionService.findAllBySociedad(sociedad);

            Map<Long, List<TareaCumplida>> tareasCumplidasMananaMap = new HashMap<>();
            Map<Long, List<TareaCumplida>> tareasCumplidasTardeMap = new HashMap<>();

            for (Ubicacion ubicacion : ubicaciones) {
                List<TareaCumplida> tareasCumplidasManana = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurno(ubicacion, fecha.atStartOfDay(), Turno.MANANA);
                List<TareaCumplida> tareasCumplidasTarde = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurno(ubicacion, fecha.atStartOfDay(), Turno.TARDE);

                tareasCumplidasMananaMap.put(ubicacion.getId(), tareasCumplidasManana);
                tareasCumplidasTardeMap.put(ubicacion.getId(), tareasCumplidasTarde);

                Sheet sheet = workbook.createSheet(cleanSheetName(ubicacion.getName()));
                // Aquí, implementa el método para llenar la hoja de cálculo con los datos
                fillSheetWithData(sheet, workbook, ubicacion, tareasCumplidasManana, tareasCumplidasTarde, sociedadSeleccionadaId);
            }

            workbook.write(response.getOutputStream());
        }
    }
    private void fillSheetWithData(Sheet sheet, Workbook workbook, Ubicacion ubicacion, List<TareaCumplida> tareasCumplidasManana, List<TareaCumplida> tareasCumplidasTarde, String sociedadId) {

        // Estilo para los títulos de "Mañana" y "Tarde"
        CellStyle turnTitleStyle = workbook.createCellStyle();
        Font turnTitleFont = workbook.createFont();
        turnTitleFont.setBold(true);
        turnTitleFont.setColor(IndexedColors.WHITE.getIndex());
        turnTitleFont.setFontHeightInPoints((short) 14);
        turnTitleStyle.setFont(turnTitleFont);
        turnTitleStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        turnTitleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Estilo para el título de la ubicación
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 18);
        titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());
        titleStyle.setFont(titleFont);

        // Este código debería estar en tu método fillSheetWithData justo después de definir los estilos
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

        // Filtrar las listas para excluir la tarea de "¿Quien ha trabajado en este turno?"
        tareasCumplidasManana = tareasCumplidasManana.stream()
                .filter(tarea -> !tarea.getTarea().getName().equals("¿Quien ha trabajado en este turno?"))
                .collect(Collectors.toList());

        tareasCumplidasTarde = tareasCumplidasTarde.stream()
                .filter(tarea -> !tarea.getTarea().getName().equals("¿Quien ha trabajado en este turno?"))
                .collect(Collectors.toList());

        // Escribir el nombre de la ubicación
        Row ubicacionRow = sheet.createRow(0);
        ubicacionRow.setHeightInPoints(25);
        Cell ubicacionCell = ubicacionRow.createCell(0);
        ubicacionCell.setCellValue(ubicacion.getName());
        ubicacionCell.setCellStyle(titleStyle);  // Aplica el estilo a la celda de ubicación


        // Escribir título de turno Mañana y la lista de trabajadores
        Row titleRow = sheet.createRow(1);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(turnTitleStyle);
        titleCell.setCellValue("Mañana");

        Cell trabajadoresCell = titleRow.createCell(1);
        trabajadoresCell.setCellValue(trabajadoresManana);

        // Escribir las cabeceras de la tabla de Turno Mañana
        Row headerRow = sheet.createRow(2);
        writeTableHeaders(sheet, headerRow);

        // Escribir las filas de datos de Turno Mañana
        int rowIndex = 3;
        for (TareaCumplida tareaCumplida : tareasCumplidasManana) {
            Row row = sheet.createRow(rowIndex++);
            writeTareaCumplidaData(workbook, row, tareaCumplida);
        }

        // Espacio entre las tablas
        rowIndex++;

        // Escribir título de turno Tarde y la lista de trabajadores
        titleRow = sheet.createRow(rowIndex++);
        titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(turnTitleStyle);
        titleCell.setCellValue("Tarde");

        trabajadoresCell = titleRow.createCell(1);
        trabajadoresCell.setCellValue(trabajadoresTarde);

        // Escribir las cabeceras de la tabla de Turno Tarde
        headerRow = sheet.createRow(rowIndex++);
        writeTableHeaders(sheet, headerRow);

        // Escribir las filas de datos de Turno Tarde
        for (TareaCumplida tareaCumplida : tareasCumplidasTarde) {
            Row row = sheet.createRow(rowIndex++);
            writeTareaCumplidaData(workbook, row, tareaCumplida);
        }
    }

    private void writeTableHeaders(Sheet sheet, Row headerRow) {
        String[] headers = {"Nombre", "Descripción", "Hecha", "Trabajador", "Comentario"};

        // Crear estilo de las cabeceras
        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        Font headerFont = sheet.getWorkbook().createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Después de escribir las cabeceras de la tabla (en writeTableHeaders)
        sheet.setColumnWidth(0, 25 * 256); // A
        sheet.setColumnWidth(1, 35 * 256); // B
        sheet.setColumnWidth(3, 25 * 256); // D
        sheet.setColumnWidth(4, 35 * 256); // E

        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(headers[i]);
            // Aplicar estilo a las celdas de encabezado
            headerCell.setCellStyle(headerStyle);
        }
    }



    private void writeTareaCumplidaData(Workbook workbook, Row row, TareaCumplida tareaCumplida) {
        // Estilo para las columnas "Nombre", "Hecha" y "Trabajador"
        CellStyle boldStyle = workbook.createCellStyle();
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldStyle.setFont(boldFont);

        // Estilo para centrar y poner en negrita el contenido en la celda
        CellStyle centerBoldStyle = workbook.createCellStyle();
        centerBoldStyle.setFont(boldFont);
        centerBoldStyle.setAlignment(HorizontalAlignment.CENTER);
        centerBoldStyle.setVerticalAlignment(VerticalAlignment.CENTER); // Agregar alineación vertical al estilo

        Cell cell = row.createCell(0);
        cell.setCellValue(tareaCumplida.getTarea().getName());
        cell.setCellStyle(centerBoldStyle);

        cell = row.createCell(1);
        cell.setCellValue(tareaCumplida.getTarea().getDescripcion());

        // En writeTareaCumplidaData, después de escribir la descripción
        CellStyle wrapStyle = workbook.createCellStyle();
        wrapStyle.setWrapText(true);
        cell.setCellStyle(wrapStyle);

        cell = row.createCell(2);
        cell.setCellValue(tareaCumplida.isCumplida() ? "SI" : "NO");
        cell.setCellStyle(centerBoldStyle);

        cell = row.createCell(3);
        cell.setCellValue(tareaCumplida.isCumplida() ? tareaCumplida.getWorker().getName() : "");
        cell.setCellStyle(centerBoldStyle);

        cell = row.createCell(4);
        cell.setCellValue(tareaCumplida.getComentario());
    }

    private String cleanSheetName(String name) {
        return name.replaceAll("[:\\\\/?*\\[\\]]", "_");
    }

    private void validateSociedadId(Long sociedadId) {
        if (!sociedadService.existsById(sociedadId)) {
            throw new NotFoundException("Sociedad no encontrada con el ID: " + sociedadId);
        }
    }
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

