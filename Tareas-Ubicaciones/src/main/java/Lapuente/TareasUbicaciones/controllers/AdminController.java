package Lapuente.TareasUbicaciones.controllers;


import Lapuente.TareasUbicaciones.DTOs.*;
import Lapuente.TareasUbicaciones.controllers.interfaces.AdminControllerInterface;
import Lapuente.TareasUbicaciones.entities.*;
import Lapuente.TareasUbicaciones.services.interfaces.TareaCumplidaServiceInterface;
import Lapuente.TareasUbicaciones.services.interfaces.TareaServiceInterface;
import Lapuente.TareasUbicaciones.services.interfaces.UbicacionServiceInterface;
import Lapuente.TareasUbicaciones.services.interfaces.WorkerServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @PostMapping("/workers")
    @ResponseStatus(HttpStatus.CREATED)
    public Worker addWorker(@RequestBody WorkerDTO workerDTO) {
        return workerService.saveWorker(workerDTO);
    }

    @GetMapping("/workers")
    @ResponseStatus(HttpStatus.OK)
    public List<Worker> getAllWorkers() {
        return workerService.getAllWorkers();
    }


    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/tareas")
    @ResponseStatus(HttpStatus.OK)
    public List<Tarea> getAllTareas() {
        return tareaService.getAllTareas();
    }

    @PostMapping("/tareas")
    @ResponseStatus(HttpStatus.CREATED)
    public Tarea addTarea(@RequestBody TareaDTO tareaDTO) {
        return tareaService.saveTarea(tareaDTO);
    }

    @GetMapping("/ubicaciones")
    @ResponseStatus(HttpStatus.OK)
    public List<Ubicacion> getAllUbicaciones() {
        return ubicacionService.findAll();
    }

    @PostMapping("/ubicaciones")
    @ResponseStatus(HttpStatus.CREATED)
    public Ubicacion addUbicacion(@RequestBody UbicacionDTO ubicacionDTO) {
        return ubicacionService.save(ubicacionDTO);
    }

    @GetMapping("/informes/diarios/{ubicacionId}/{fecha}")
    @ResponseStatus(HttpStatus.OK)
    public List<TareaCumplida> getInformesDiarios(@PathVariable Long ubicacionId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        LocalDateTime fechaInicio = fecha.atStartOfDay();
        LocalDateTime fechaFin = fechaInicio.plusDays(1);
        return workerService.getTareasCumplidasByUbicacionYPeriodo(ubicacionId, fechaInicio, fechaFin);
    }

    @GetMapping("/informes/mensuales/{ubicacionId}/{year}/{month}")
    @ResponseStatus(HttpStatus.OK)
    public List<TareaCumplida> getInformesMensuales(@PathVariable Long ubicacionId, @PathVariable int year, @PathVariable int month) {
        LocalDate fechaInicio = LocalDate.of(year, month, 1);
        LocalDate fechaFin = fechaInicio.plusMonths(1);
        return workerService.getTareasCumplidasByUbicacionYPeriodo(ubicacionId, fechaInicio.atStartOfDay(), fechaFin.atStartOfDay());
    }

    @PatchMapping("/tareasCumplidas/{tareaCumplidaId}")
    @ResponseStatus(HttpStatus.OK)
    public TareaCumplida updateTareaCumplida(@PathVariable Long tareaCumplidaId, @RequestBody TareaCumplidaDTO tareaCumplidaDTO) {
        return tareaCumplidaService.updateTareaCumplida(tareaCumplidaId, tareaCumplidaDTO);
    }

    @GetMapping("/informes/periodo")
    @ResponseStatus(HttpStatus.OK)
    public List<TareaCumplida> getTareasCumplidasByUbicacionYPeriodo(
            @RequestParam Long ubicacionId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return workerService.getTareasCumplidasByUbicacionYPeriodo(ubicacionId, fechaInicio, fechaFin);
    }


    @PostMapping("/ubicaciones/{ubicacionId}/tareas")
    @ResponseStatus(HttpStatus.CREATED)
    public Ubicacion addTareaAUbicacion(@PathVariable Long ubicacionId, @RequestBody TareaDTO tareaDTO) {
        return ubicacionService.addTareaAUbicacion(ubicacionId, tareaDTO);
    }

    @PatchMapping("/ubicaciones/{ubicacionId}/tareas")
    @ResponseStatus(HttpStatus.OK)
    public Ubicacion updateTareasDeUbicacion(@PathVariable Long ubicacionId, @RequestBody Set<TareaDTO> tareasDTO) {
        return ubicacionService.updateTareasDeUbicacion(ubicacionId, tareasDTO);
    }
    @GetMapping("/adminsmenu")
    public String adminMenu() {
        return "adminsmenu";
    }
}

