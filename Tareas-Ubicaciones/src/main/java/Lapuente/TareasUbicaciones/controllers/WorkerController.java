package Lapuente.TareasUbicaciones.controllers;

import Lapuente.TareasUbicaciones.ENUMs.Turno;
import Lapuente.TareasUbicaciones.controllers.interfaces.WorkerControllerInterface;
import Lapuente.TareasUbicaciones.entities.Tarea;
import Lapuente.TareasUbicaciones.entities.TareaCumplida;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.entities.Worker;
import Lapuente.TareasUbicaciones.repositories.TareaCumplidaRepository;
import Lapuente.TareasUbicaciones.repositories.UbicacionRepository;
import Lapuente.TareasUbicaciones.repositories.WorkerRepository;
import Lapuente.TareasUbicaciones.services.TareaCumplidaService;
import Lapuente.TareasUbicaciones.services.interfaces.TareaServiceInterface;
import Lapuente.TareasUbicaciones.services.interfaces.UbicacionServiceInterface;
import Lapuente.TareasUbicaciones.services.interfaces.WorkerServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/worker")
public class WorkerController implements WorkerControllerInterface {

    @Autowired
    private WorkerServiceInterface workerService;

    @Autowired
    private UbicacionServiceInterface ubicacionService;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private TareaCumplidaRepository tareaCumplidaRepository;

    @Autowired
    private TareaCumplidaService tareaCumplidaService;

    @GetMapping("/ubicaciones")
    public String getAllUbicaciones(Model model) {
        List<Ubicacion> ubicaciones = ubicacionService.findAll();
        model.addAttribute("ubicaciones", ubicaciones);
        return "workersubicaciones";
    }
    @GetMapping("/ubicaciones/{ubicacionId}/selectturno")
    public String selectTurno(@PathVariable Long ubicacionId, Model model) {
        model.addAttribute("ubicacionId", ubicacionId);
        return "workersturno";
    }

    @GetMapping("/ubicaciones/{ubicacionId}/tareas")
    public String getTareasByUbicacion(@PathVariable Long ubicacionId,
                                       @RequestParam Turno turno,
                                       Model model) {
        List<Tarea> tareas = ubicacionService.getTareasByUbicacionId(ubicacionId);
        Ubicacion ubicacion = ubicacionService.findById(ubicacionId);
        LocalDateTime fechaActual = LocalDateTime.now();
        List<TareaCumplida> tareasCumplidas = tareaCumplidaService.findTareasNoInformadasByUbicacionAndFechaAndTurno(ubicacion, fechaActual, turno);

        model.addAttribute("ubicacionId", ubicacionId);
        model.addAttribute("turno", turno);
        model.addAttribute("ubicacion", ubicacion);
        model.addAttribute("fechaActual", fechaActual);

        // Crea un mapa que relacione la Tarea con su correspondiente TareaCumplida
        Map<Tarea, TareaCumplida> tareaTareaCumplidaMap = new HashMap<>();
        for (Tarea tarea : tareas) {
            TareaCumplida tareaCumplida = tareasCumplidas.stream()
                    .filter(tc -> tc.getTarea().getId().equals(tarea.getId()))
                    .findFirst()
                    .orElse(null);
            tareaTareaCumplidaMap.put(tarea, tareaCumplida);
        }

        // Añade el mapa al modelo
        model.addAttribute("tareaTareaCumplidaMap", tareaTareaCumplidaMap);

        // Obtén el comentario para la ubicación, fecha y turno
        Optional<String> comentario = tareaCumplidaService.findComentarioByUbicacionAndFechaAndTurno(ubicacion, fechaActual, turno);
        model.addAttribute("comentario", comentario.orElse(""));

        return "workerstareas";
    }

    @PostMapping("/ubicaciones/{ubicacionId}/tareas/informar")
    public String informarTareasCumplidas(@PathVariable Long ubicacionId,
                                          @RequestParam(value = "tareaId", required = false) List<Long> tareasCumplidasIds,
                                          @RequestParam Turno turno,
                                          @AuthenticationPrincipal UserDetails userDetails,
                                          @RequestParam(required = false) String comentario,
                                          RedirectAttributes redirectAttributes) {
        try {
            workerService.informarTareasCumplidas(ubicacionId, turno, tareasCumplidasIds, userDetails, comentario);
            redirectAttributes.addFlashAttribute("message", "Tareas guardadas correctamente.");
            return "redirect:/worker/ubicaciones";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar las tareas: " + e.getMessage());
            return "redirect:/worker/ubicaciones/" + ubicacionId + "/tareas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar las tareas.");
            return "redirect:/worker/ubicaciones/" + ubicacionId + "/tareas";
        }
    }

    @Override
    @PostMapping("/password")
    public void cambiarPassword(@RequestParam Long workerId, @RequestParam String oldPassword, @RequestParam String newPassword) {
        workerService.cambiarPassword(workerId, oldPassword, newPassword);
    }

    @GetMapping("/workersmenu")
    public String workerMenu() {
        return "workersmenu";
    }

}
