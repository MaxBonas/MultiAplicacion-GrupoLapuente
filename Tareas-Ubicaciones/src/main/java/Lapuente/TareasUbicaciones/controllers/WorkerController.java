package Lapuente.TareasUbicaciones.controllers;

import Lapuente.TareasUbicaciones.controllers.interfaces.WorkerControllerInterface;
import Lapuente.TareasUbicaciones.entities.Tarea;
import Lapuente.TareasUbicaciones.entities.TareasDiarias;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.entities.Worker;
import Lapuente.TareasUbicaciones.services.interfaces.TareaServiceInterface;
import Lapuente.TareasUbicaciones.services.interfaces.TareasDiariasServiceInterface;
import Lapuente.TareasUbicaciones.services.interfaces.UbicacionServiceInterface;
import Lapuente.TareasUbicaciones.services.interfaces.WorkerServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WorkerController implements WorkerControllerInterface {

    @Autowired
    private WorkerServiceInterface workerService;

    @Autowired
    private TareaServiceInterface tareaService;

    @Autowired
    private UbicacionServiceInterface ubicacionService;

    @Override
    @GetMapping("/worker/ubicaciones")
    public List<Ubicacion> getAllUbicaciones() {
        return ubicacionService.findAll();
    }

    @Override
    @GetMapping("/worker/ubicaciones/{ubicacionId}/tareas")
    public List<Tarea> getTareasByUbicacion(@PathVariable Long ubicacionId) {
        return tareaService.getTareasByUbicacion(ubicacionId);
    }

    @Override
    @GetMapping("/worker/tareas/{tareaId}")
    public Tarea getTareaById(@PathVariable Long tareaId) {
        return tareaService.getTareaById(tareaId);
    }

    @Override
    @PostMapping("/worker/tareas/{tareaId}/ubicaciones/{ubicacionId}/confirmar")
    public void confirmarTarea(@PathVariable Long tareaId, @PathVariable Long ubicacionId, @RequestParam boolean tareaCumplida) {
        tareaService.confirmarTarea(tareaId, ubicacionId, boolean tareaCumplida);
    }

    @Override
    @PostMapping("/worker/ubicaciones/{ubicacionId}/tareas")
    public void asignarTareas(@PathVariable Long ubicacionId, @RequestBody List<Long> tareaId, @AuthenticationPrincipal UserDetails userDetails
        workerService.asignarTareas(ubicacionId, tareaId, userDetails);
    }

    @Override
    @GetMapping("/worker/tareas_diarias")
    public List<TareasDiarias> getTareasDiarias() {
        return workerService.getTareasDiarias();
    }

    @Override
    @PostMapping("/worker/password")
    public void cambiarPassword(@RequestBody CambioPassword cambioPassword) {
        workerService.cambiarPassword(cambioPassword);
    }

    @Override
    @PostMapping("/worker/tareas_diarias/comentarios")
    public void agregarComentariosInforme(@RequestBody ComentariosInforme comentariosInforme) {
        workerService.agregarComentariosInforme(comentariosInforme);
    }

    @Override
    @GetMapping("/worker/tareas_diarias/{fecha}")
    public List<TareasDiarias> getTareasDiariasSiguientes(@PathVariable LocalDate fecha) {
        return workerService.getTareasDiariasSiguientes(fecha);
    }
}