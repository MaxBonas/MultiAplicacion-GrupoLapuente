package MultiAplicacion.controllers;

import MultiAplicacion.ENUMs.Turno;
import MultiAplicacion.controllers.interfaces.WorkerControllerInterface;
import MultiAplicacion.entities.*;
import MultiAplicacion.repositories.TareaCumplidaRepository;
import MultiAplicacion.repositories.UbicacionRepository;
import MultiAplicacion.repositories.WorkerRepository;
import MultiAplicacion.services.TareaCumplidaService;
import MultiAplicacion.services.interfaces.UbicacionServiceInterface;
import MultiAplicacion.services.interfaces.WorkerServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/worker/{sociedadId}")
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
    public String getAllUbicaciones(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Worker worker = workerService.findByName(userDetails.getUsername());
        Sociedad workerSociedad = worker.getSociedad();
        List<Ubicacion> ubicaciones = ubicacionService.findAllBySociedad(workerSociedad);
        model.addAttribute("worker", worker);
        model.addAttribute("ubicaciones", ubicaciones);
        return "workers/workersubicaciones";
    }

    @GetMapping("/ubicaciones/{ubicacionId}/selectturno")
    public String selectTurno(@PathVariable Long ubicacionId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Worker worker = workerService.findByName(userDetails.getUsername());
        model.addAttribute("worker", worker);
        model.addAttribute("ubicacionId", ubicacionId);
        return "workers/workersturno";
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

        return "workers/workerstareas";
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
            return "redirect:/worker/" + workerRepository.findByName(userDetails.getUsername()).orElseThrow().getSociedad().getId() + "/ubicaciones";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar las tareas: " + e.getMessage());
            return "redirect:/worker/" + workerRepository.findByName(userDetails.getUsername()).orElseThrow().getSociedad().getId() + "/ubicaciones/" + ubicacionId + "/tareas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar las tareas.");
            return "redirect:/worker/" + workerRepository.findByName(userDetails.getUsername()).orElseThrow().getSociedad().getId() + "/ubicaciones/" + ubicacionId + "/tareas";
        }
    }

    @Override
    @PostMapping("/password")
    public void cambiarPassword(@RequestParam Long workerId, @RequestParam String oldPassword, @RequestParam String newPassword) {
        workerService.cambiarPassword(workerId, oldPassword, newPassword);
    }

    @GetMapping("/workersmenu")
    public String workerMenu(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("worker", workerService.findByName(userDetails.getUsername()));
        return "workers/workersmenu";
    }

}
