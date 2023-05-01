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
import java.util.*;
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
    @Autowired
    private ServletContext servletContext;


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
    @PostMapping("/informes/diario/export")
    public void exportarInformeDiarioExcel(@RequestParam("fecha") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha, HttpServletResponse response, HttpSession session) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Informe_Diario_Tareas" + fecha + ".xlsx");

        try (Workbook workbook = new XSSFWorkbook()) {
            List<Ubicacion> ubicaciones = ubicacionService.findAll();
            Map<Long, List<TareaCumplida>> tareasCumplidasMananaMap = new HashMap<>();
            Map<Long, List<TareaCumplida>> tareasCumplidasTardeMap = new HashMap<>();

            for (Ubicacion ubicacion : ubicaciones) {
                List<TareaCumplida> tareasCumplidasManana = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurno(ubicacion, fecha.atStartOfDay(), Turno.MANANA);
                List<TareaCumplida> tareasCumplidasTarde = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurno(ubicacion, fecha.atStartOfDay(), Turno.TARDE);

                tareasCumplidasMananaMap.put(ubicacion.getId(), tareasCumplidasManana);
                tareasCumplidasTardeMap.put(ubicacion.getId(), tareasCumplidasTarde);

                Sheet sheet = workbook.createSheet(cleanSheetName(ubicacion.getName()));
                String sociedadSeleccionadaId = session.getAttribute("sociedadSeleccionadaId") != null ? session.getAttribute("sociedadSeleccionadaId").toString() : "1"; // O cualquier otro valor predeterminado que desees utilizar
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

        // En la función fillSheetWithData, antes de crear la ubicacionRow
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 18);
        titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());
        titleStyle.setFont(titleFont);

        // Escribir el nombre de la ubicación
        Row ubicacionRow = sheet.createRow(0);
        // Después de crear la fila de ubicación (ubicacionRow)
        ubicacionRow.setHeightInPoints(25);
        Cell ubicacionCell = ubicacionRow.createCell(0);
        ubicacionCell.setCellValue(ubicacion.getName());
        // Aplicar estilo a la celda de ubicación
        ubicacionCell.setCellStyle(titleStyle);

        // Escribir título de turno Mañana
        Row titleRow = sheet.createRow(1);
        Cell titleCell = titleRow.createCell(0);
        // Aplicar estilo a los títulos de turno
        titleCell.setCellStyle(turnTitleStyle);

        titleCell.setCellValue("Mañana");

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

        // Escribir título de turno Tarde
        titleRow = sheet.createRow(rowIndex++);
        titleCell = titleRow.createCell(0);
        // Aplicar estilo a los títulos de turno
        titleCell.setCellStyle(turnTitleStyle);

        titleCell.setCellValue("Tarde");

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
}

