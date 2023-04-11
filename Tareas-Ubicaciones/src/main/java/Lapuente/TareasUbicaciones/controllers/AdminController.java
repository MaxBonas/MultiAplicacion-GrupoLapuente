package Lapuente.TareasUbicaciones.controllers;


import Lapuente.TareasUbicaciones.DTOs.*;
import Lapuente.TareasUbicaciones.ENUMs.Turno;
import Lapuente.TareasUbicaciones.controllers.interfaces.AdminControllerInterface;
import Lapuente.TareasUbicaciones.entities.*;
import Lapuente.TareasUbicaciones.repositories.*;
import Lapuente.TareasUbicaciones.services.InformeService;
import Lapuente.TareasUbicaciones.services.interfaces.TareaCumplidaServiceInterface;
import Lapuente.TareasUbicaciones.services.interfaces.TareaServiceInterface;
import Lapuente.TareasUbicaciones.services.interfaces.UbicacionServiceInterface;
import Lapuente.TareasUbicaciones.services.interfaces.WorkerServiceInterface;
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
@RequestMapping("/admin")
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

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/adminsmenu")
    public String adminMenu() {
        return "adminsmenu";
    }

    @GetMapping("/workers")
    public String getAllWorkers(Model model) {
        List<Worker> workers = workerService.getAllWorkers();
        model.addAttribute("workers", workers);
        return "workers";
    }

    @PostMapping("/crear-trabajador")
    public String addWorker(@ModelAttribute WorkerDTO workerDTO, RedirectAttributes redirectAttributes) {
        workerService.saveWorker(workerDTO);
        redirectAttributes.addFlashAttribute("message", "Trabajador creado exitosamente");
        return "redirect:/admin/workers";
    }
    @GetMapping("/crear-trabajador")
    public String showCreateWorkerForm(Model model) {
        model.addAttribute("workerDTO", new WorkerDTO());
        return "crear-trabajador";
    }


    @GetMapping("/tareas")
    public String getAllTareas(Model model) {
        List<Tarea> tareas = tareaService.getAllTareas();
        model.addAttribute("tareas", tareas);
        return "tareas";
    }

    @PostMapping("/tareas")
    public String addTarea(@ModelAttribute TareaDTO tareaDTO, RedirectAttributes redirectAttributes) {
        tareaService.saveTarea(tareaDTO);
        redirectAttributes.addFlashAttribute("message", "Tarea creada exitosamente");
        return "redirect:/admin/tareas";
    }

    @GetMapping("/ubicaciones")
    public String getAllUbicaciones(Model model) {
        List<Ubicacion> ubicaciones = ubicacionService.findAll();
        model.addAttribute("ubicaciones", ubicaciones);
        return "ubicaciones";
    }

    @PostMapping("/ubicaciones")
    public String addUbicacion(@ModelAttribute UbicacionDTO ubicacionDTO, RedirectAttributes redirectAttributes) {
        ubicacionService.save(ubicacionDTO);
        redirectAttributes.addFlashAttribute("message", "Ubicación creada exitosamente");
        return "redirect:/admin/ubicaciones";
    }

    // Para actualizar un trabajador
    @PutMapping("/workers/{id}")
    public String updateWorker(@PathVariable Long id, @ModelAttribute WorkerDTO workerDTO, RedirectAttributes redirectAttributes) {
        workerService.updateWorker(id, workerDTO);
        redirectAttributes.addFlashAttribute("message", "Trabajador actualizado exitosamente");
        return "redirect:/admin/workers";
    }

    // Para actualizar una tarea
    @PutMapping("/tareas/{id}")
    public String updateTarea(@PathVariable Long id, @ModelAttribute TareaDTO tareaDTO, RedirectAttributes redirectAttributes) {
        tareaService.updateTarea(id, tareaDTO);
        redirectAttributes.addFlashAttribute("message", "Tarea actualizada exitosamente");
        return "redirect:/admin/tareas";
    }

    // Para actualizar una ubicación
    @PutMapping("/ubicaciones/{id}")
    public String updateUbicacion(@PathVariable Long id, @ModelAttribute UbicacionDTO ubicacionDTO, RedirectAttributes redirectAttributes) {
        ubicacionService.updateUbicacion(id, ubicacionDTO);
        redirectAttributes.addFlashAttribute("message", "Ubicación actualizada exitosamente");
        return "redirect:/admin/ubicaciones";
    }

    // Para eliminar un trabajador
    @DeleteMapping("/workers/{id}")
    public String deleteWorker(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        workerService.deleteWorkerById(id);
        redirectAttributes.addFlashAttribute("message", "Trabajador eliminado exitosamente");
        return "redirect:/admin/workers";
    }

    // Para eliminar una tarea
    @DeleteMapping("/tareas/{id}")
    public String deleteTarea(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        tareaService.deleteTareaById(id);
        redirectAttributes.addFlashAttribute("message", "Tarea eliminada exitosamente");
        return "redirect:/admin/tareas";
    }

    // Para eliminar una ubicación
    @DeleteMapping("/ubicaciones/{id}")
    public String deleteUbicacion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ubicacionService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Ubicación eliminada exitosamente");
        return "redirect:/admin/ubicaciones";
    }

    // Para ver los detalles de un trabajador
    @GetMapping("/workers/{id}")
    public String getWorker(@PathVariable Long id, Model model) {
        Worker worker = workerService.getWorkerById(id);
        model.addAttribute("worker", worker);
        return "workerDetails";
    }

    // Para ver los detalles de una tarea
    @GetMapping("/tareas/{id}")
    public String getTarea(@PathVariable Long id, Model model) {
        Tarea tarea = tareaService.getTareaById(id);
        model.addAttribute("tarea", tarea);
        return "tareaDetails";
    }

    // Para ver los detalles de una ubicación
    @GetMapping("/ubicaciones/{id}")
    public String getUbicacion(@PathVariable Long id, Model model) {
        Ubicacion ubicacion = ubicacionService.findById(id);
        model.addAttribute("ubicacion", ubicacion);
        return "ubicacionDetails";
    }

    @PostMapping("/ubicaciones/{ubicacionId}/tareas")
    @ResponseStatus(HttpStatus.CREATED)
    public String addTareaAUbicacion(@PathVariable Long ubicacionId, @ModelAttribute TareaDTO tareaDTO, RedirectAttributes redirectAttributes) {
        ubicacionService.addTareaAUbicacion(ubicacionId, tareaDTO);
        redirectAttributes.addFlashAttribute("message", "Tarea añadida a la ubicación exitosamente");
        return "redirect:/admin/ubicaciones";
    }

    @PatchMapping("/ubicaciones/{ubicacionId}/tareas")
    @ResponseStatus(HttpStatus.OK)
    public String updateTareasDeUbicacion(@PathVariable Long ubicacionId, @RequestBody Set<TareaDTO> tareasDTO, RedirectAttributes redirectAttributes) {
        ubicacionService.updateTareasDeUbicacion(ubicacionId, tareasDTO);
        redirectAttributes.addFlashAttribute("message", "Tareas de la ubicación actualizadas exitosamente");
        return "redirect:/admin/ubicaciones";
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

