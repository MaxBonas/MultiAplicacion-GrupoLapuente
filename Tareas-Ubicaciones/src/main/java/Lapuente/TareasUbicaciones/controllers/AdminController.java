package Lapuente.TareasUbicaciones.controllers;


import Lapuente.TareasUbicaciones.DTOs.*;
import Lapuente.TareasUbicaciones.ENUMs.Turno;
import Lapuente.TareasUbicaciones.controllers.interfaces.AdminControllerInterface;
import Lapuente.TareasUbicaciones.entities.*;
import Lapuente.TareasUbicaciones.services.InformeService;
import Lapuente.TareasUbicaciones.services.interfaces.TareaCumplidaServiceInterface;
import Lapuente.TareasUbicaciones.services.interfaces.TareaServiceInterface;
import Lapuente.TareasUbicaciones.services.interfaces.UbicacionServiceInterface;
import Lapuente.TareasUbicaciones.services.interfaces.WorkerServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController implements AdminControllerInterface{

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

    @PostMapping("/workers")
    public String addWorker(@ModelAttribute WorkerDTO workerDTO, RedirectAttributes redirectAttributes) {
        workerService.saveWorker(workerDTO);
        redirectAttributes.addFlashAttribute("message", "Trabajador creado exitosamente");
        return "redirect:/admin/workers";
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

    @GetMapping("/informes/diario")
    public String getInformeDiario(@RequestParam("fecha") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha, Model model) {
        LocalDateTime inicioDelDia = fecha.atStartOfDay();
        LocalDateTime finDelDia = fecha.atTime(LocalTime.MAX);
        List<Informe> informes = informeService.findByFechaBetween(inicioDelDia, finDelDia);
        model.addAttribute("informes", informes);
        return "informes/informeDiario";
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

