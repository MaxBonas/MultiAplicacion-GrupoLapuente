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

    @GetMapping("/ubicaciones/asignar/{tareaId}")
    public String showAssignTareaForm(@PathVariable Long tareaId, Model model) {
        Tarea tarea = tareaService.getTareaById(tareaId);
        List<Ubicacion> ubicaciones = ubicacionService.findAllAvailableForTarea(tareaId); // Asegúrate de implementar este método en ubicacionService
        model.addAttribute("tarea", tarea);
        model.addAttribute("ubicaciones", ubicaciones);
        return "admins/asignar-tarea";
    }

    @PostMapping("/ubicaciones/asignar/{tareaId}")
    public String assignTareaToUbicacion(@PathVariable Long tareaId, @RequestParam Long ubicacionId, RedirectAttributes redirectAttributes) {
        // Obtiene la tarea como TareaDTO
        Tarea tarea = tareaService.getTareaById(tareaId);
        TareaDTO tareaDTO = new TareaDTO();
        tareaDTO.setId(tarea.getId());
        tareaDTO.setName(tarea.getName());
        tareaDTO.setDescripcion(tarea.getDescripcion());

        // Asigna la tarea a la ubicación utilizando el método existente
        ubicacionService.addTareaAUbicacion(ubicacionId, tareaDTO);
        redirectAttributes.addFlashAttribute("message", "Tarea asignada a la ubicación exitosamente");
        return "redirect:/admin/{sociedadId}/tareas";
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

    @PostMapping("/tareas")
    public String addTarea(@ModelAttribute TareaDTO tareaDTO, RedirectAttributes redirectAttributes) {
        tareaService.saveTarea(tareaDTO);
        redirectAttributes.addFlashAttribute("message", "Tarea creada exitosamente");
        return "redirect:/admin/{sociedadId}/tareas";
    }
    @GetMapping("/crear-tarea")
    public String showCreateTareaForm(Model model) {
        model.addAttribute("tareaDTO", new TareaDTO());
        return "admins/crear-tarea";
    }
    @GetMapping("/ubicaciones")
    public String getAllUbicaciones(@PathVariable("sociedadId") Long sociedadId, Model model) {
        // Encuentra la sociedad por ID o arroja una excepción si no se encuentra.
        Sociedad sociedad = sociedadService.findById(sociedadId)
                .orElseThrow(() -> new NotFoundException("Sociedad no encontrada con el ID: " + sociedadId));

        List<Ubicacion> ubicaciones = ubicacionService.findAllBySociedad(sociedad);
        model.addAttribute("ubicaciones", ubicaciones);

        Map<String, List<Pair<Long, Pair<String, String>>>> tareasAgrupadasPorUbicacion = ubicaciones.stream()
                .collect(Collectors.toMap(
                        Ubicacion::getName,
                        ubicacion -> ubicacion.getTareas().stream()
                                .map(tarea -> Pair.of(tarea.getId(), Pair.of(tarea.getName(), tarea.getDescripcion())))
                                .collect(Collectors.toList())
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

    // Para ver los detalles de un trabajador
    @GetMapping("/workers/{id}")
    public String getWorker(@PathVariable Long id, Model model) {
        Worker worker = workerService.getWorkerById(id);
        model.addAttribute("worker", worker);
        return "admins/workerDetails";
    }

    // Para ver los detalles de una tarea
    @GetMapping("/tareas/{id}")
    public String getTarea(@PathVariable Long id, Model model) {
        Tarea tarea = tareaService.getTareaById(id);
        model.addAttribute("tarea", tarea);
        return "admins/tareaDetails";
    }

    // Para ver los detalles de una ubicación
    @GetMapping("/ubicaciones/{id}")
    public String getUbicacion(@PathVariable Long id, Model model) {
        Ubicacion ubicacion = ubicacionService.findById(id);
        model.addAttribute("ubicacion", ubicacion);
        return "admins/ubicacionDetails";
    }

    @PostMapping("/ubicaciones/{ubicacionId}/tareas")
    @ResponseStatus(HttpStatus.CREATED)
    public String addTareaAUbicacion(@PathVariable Long ubicacionId, @ModelAttribute TareaDTO tareaDTO, RedirectAttributes redirectAttributes) {
        ubicacionService.addTareaAUbicacion(ubicacionId, tareaDTO);
        redirectAttributes.addFlashAttribute("message", "Tarea añadida a la ubicación exitosamente");
        return "redirect:/admin/{sociedadId}/ubicaciones";
    }

    @GetMapping("/informes/diario/request")
    public String getInformeDiarioRequest(Model model) {
        return "informes/informeDiarioRequest";
    }

    @GetMapping("/informes/diario")
    public ResponseEntity<byte[]> getInformeDiario(
            @RequestParam(value = "fecha")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @NotBlank(message = "La fecha es obligatoria")
            LocalDate fecha) {

        LocalDateTime inicioDelDia = fecha.atStartOfDay();
        LocalDateTime finDelDia = fecha.atTime(LocalTime.MAX);

        List<InformeDTO> informeDiario = obtenerDatosInformeDiario(inicioDelDia, finDelDia);
        return generarInformeExcel(informeDiario);
    }

    private ResponseEntity<byte[]> generarInformeExcel(List<InformeDTO> informeDiario) {
        // Crear el informe Excel
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Informe Diario");

            // Encabezados
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Ubicación", "Hora", "Turno", "Trabajador", "Tarea", "Cumplida"};
            for (int i = 0; i < headers.length; i++) {
                Cell headerCell = headerRow.createCell(i);
                headerCell.setCellValue(headers[i]);
            }

            // Datos
            int rowNum = 1;
            for (InformeDTO informe : informeDiario) {
                String ubicacionName = ubicacionRepository.findById(informe.getUbicacionId()).orElseThrow().getName();
                String turnoName = informe.getTurno() != null ? informe.getTurno().toString() : "";
                List<TareaCumplidaDTO> tareasCumplidas = informe.getTareasCumplidas();

                if (tareasCumplidas != null && !tareasCumplidas.isEmpty()) {
                    boolean firstRow = true;
                    for (TareaCumplidaDTO tareaCumplida : tareasCumplidas) {
                        Tarea tarea = tareaRepository.findById(tareaCumplida.getTareaId()).orElseThrow();
                        String tareaName = tarea.getName();

                        Row row = sheet.createRow(rowNum++);
                        if (firstRow) {
                            row.createCell(0).setCellValue(ubicacionName);
                            row.createCell(1).setCellValue(turnoName);
                            firstRow = false;
                        }
                        row.createCell(2).setCellValue(tareaCumplida.getFechaCumplimiento() != null ? tareaCumplida.getFechaCumplimiento().toLocalTime().toString() : "N/A");
                        row.createCell(3).setCellValue(tareaCumplida.getWorkerName() != null ? tareaCumplida.getWorkerName() : "N/A");
                        row.createCell(4).setCellValue(tareaName);
                        row.createCell(5).setCellValue(tareaCumplida.isCumplida() ? "Sí" : "No");
                    }
                } else {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(ubicacionName);
                    row.createCell(1).setCellValue(turnoName);
                    row.createCell(2).setCellValue("N/A");
                    row.createCell(3).setCellValue("N/A");
                    row.createCell(4).setCellValue("N/A");
                    row.createCell(5).setCellValue("No");
                }
            }

            // Ajustar columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Escribir el archivo Excel en un array de bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            // Crear la respuesta HTTP
            HttpHeaders headersResponse = new HttpHeaders();
            headersResponse.setContentType(
                    new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headersResponse.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            headersResponse.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=InformeDiario.xlsx");

            return new ResponseEntity<>(outputStream.toByteArray(), headersResponse, HttpStatus.OK);
        } catch (IOException e) {
            // Tratar la excepción como mejor te parezca
            throw new RuntimeException("Error al generar el informe Excel", e);
        }
    }

    private List<InformeDTO> obtenerDatosInformeDiario(LocalDateTime inicioDelDia, LocalDateTime finDelDia) {
        List<Informe> informes = informeService.findByFechaBetween(inicioDelDia, finDelDia);

        // Recopilar todas las tareas para cada ubicación
        Map<Ubicacion, List<Tarea>> tareasPorUbicacion = new HashMap<>();
        for (Informe informe : informes) {
            Ubicacion ubicacion = informe.getUbicacion();
            List<Tarea> tareas = tareaRepository.findByUbicaciones(ubicacion);
            tareasPorUbicacion.put(ubicacion, tareas);
        }

        List<InformeDTO> informeDTOs = new ArrayList<>();
        for (Informe informe : informes) {
            Ubicacion ubicacion = informe.getUbicacion();
            List<Tarea> tareas = tareasPorUbicacion.get(ubicacion);
            List<TareaCumplidaDTO> tareasCompletas = new ArrayList<>();

            for (Tarea tarea : tareas) {
                TareaCumplida tareaCumplida = informe.getTareasCumplidas().stream()
                        .filter(tc -> tc.getTarea().getId().equals(tarea.getId()))
                        .findFirst()
                        .orElse(null);

                TareaCumplidaDTO tareaDTO;
                if (tareaCumplida != null) {
                    tareaDTO = new TareaCumplidaDTO(tarea.getId(), tarea.getName(), tareaCumplida.getWorker().getId(),
                            tareaCumplida.getUbicacion().getId(), tareaCumplida.isCumplida(), tareaCumplida.getWorker().getName(),
                            tareaCumplida.getFechaCumplimiento(), tareaCumplida.getTurno());
                } else {
                    tareaDTO = new TareaCumplidaDTO(tarea.getId(), tarea.getName(), null, ubicacion.getId(), false, null, null, null);
                }
                tareasCompletas.add(tareaDTO);
            }

            // Filtrar tareasCompletas para incluir solo las que coinciden con el turno actual del informe
            List<TareaCumplidaDTO> tareasCompletasFiltradas = tareasCompletas.stream()
                    .filter(tc -> (informe.getTurno() == null && tc.getTurno() == null) || (informe.getTurno() != null && informe.getTurno().equals(tc.getTurno())))
                    .collect(Collectors.toList());

            InformeDTO informeDTO = new InformeDTO(
                    informe.getFecha(),
                    informe.getUbicacion().getId(),
                    informe.getTurno(),
                    informe.getComentario(),
                    tareasCompletasFiltradas
            );

            informeDTOs.add(informeDTO);
        }

        return informeDTOs;
    }

        @GetMapping("/informes/turno-ubicacion")
    public String getInformeTurnoUbicacion(@RequestParam("fecha") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha, @RequestParam("turno") Turno turno, @RequestParam("ubicacionId") Long ubicacionId, Model model) {
        LocalDateTime inicioDelDia = fecha.atStartOfDay();
        LocalDateTime finDelDia = fecha.atTime(LocalTime.MAX);
        List<Informe> informes = informeService.findByFechaBetweenAndTurnoAndUbicacionId(inicioDelDia, finDelDia, turno, ubicacionId);
        model.addAttribute("informes", informes);
        return "informes/informeTurnoUbicacion";
    }

    @GetMapping("/informes/mensual")
    public String getInformeMensual(@RequestParam("mes") @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes, Model model) {
        LocalDateTime inicioDelMes = mes.atDay(1).atStartOfDay();
        LocalDateTime finDelMes = mes.atEndOfMonth().atTime(LocalTime.MAX);
        List<Informe> informes = informeService.findByFechaBetween(inicioDelMes, finDelMes);
        model.addAttribute("informes", informes);
        return "informes/informeMensual";
    }
}

