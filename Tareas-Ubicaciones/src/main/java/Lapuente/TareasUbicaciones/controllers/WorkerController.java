package Lapuente.TareasUbicaciones.controllers;

import Lapuente.TareasUbicaciones.ENUMs.Turno;
import Lapuente.TareasUbicaciones.controllers.interfaces.WorkerControllerInterface;
import Lapuente.TareasUbicaciones.entities.Tarea;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.services.interfaces.TareaServiceInterface;
import Lapuente.TareasUbicaciones.services.interfaces.UbicacionServiceInterface;
import Lapuente.TareasUbicaciones.services.interfaces.WorkerServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/worker")
public class WorkerController implements WorkerControllerInterface {

    @Autowired
    private WorkerServiceInterface workerService;

    @Autowired
    private UbicacionServiceInterface ubicacionService;

    @Override
    @GetMapping("/ubicaciones")
    public List<Ubicacion> getAllUbicaciones() {
        return ubicacionService.findAll();
    }

    @Override
    @GetMapping("/ubicaciones/{ubicacionId}/tareas")
    public List<Tarea> getTareasByUbicacion(@PathVariable Long ubicacionId) {
        return ubicacionService.getTareasByUbicacionId(ubicacionId);
    }

    @Override
    @PostMapping("/ubicaciones/{ubicacionId}/tareas/informar")
    public void informarTareasCumplidas(@PathVariable Long ubicacionId,
                                        @RequestParam Turno turno,
                                        @RequestBody List<Long> tareasCumplidasIds,
                                        @AuthenticationPrincipal UserDetails userDetails,
                                        @RequestParam(required = false) String comentario) {
        workerService.informarTareasCumplidas(ubicacionId, turno, tareasCumplidasIds, userDetails, comentario);
    }

    @Override
    @PostMapping("/password")
    public void cambiarPassword(@RequestParam Long workerId, @RequestParam String oldPassword, @RequestParam String newPassword) {
        workerService.cambiarPassword(workerId, oldPassword, newPassword);
    }

    @GetMapping("/workersmenu")
    public String workersMenu() {
        return "workersmenu";
    }

    @GetMapping("/entrar")
    public String entrar() {
        return "workersmenu";
    }

}
