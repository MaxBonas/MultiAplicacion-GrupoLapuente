package MultiAplicacion.controllers;

import MultiAplicacion.DTOs.TareaCumplidaDTO;
import MultiAplicacion.DTOs.TareaCumplidaListWrapper;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/worker/{sociedadId}")
public class WorkerController implements WorkerControllerInterface {
    private final Logger logger = LoggerFactory.getLogger(WorkerController.class);

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
    public String showTareas(@PathVariable Long sociedadId, @PathVariable Long ubicacionId, @RequestParam Turno turno, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Worker worker = workerService.findByName(userDetails.getUsername());
        Ubicacion ubicacion = ubicacionService.findById(ubicacionId);
        LocalDateTime fecha = LocalDateTime.now();

        workerService.crearTareasCumplidasVacias(worker.getId(), ubicacionId, fecha, turno);
        List<TareaCumplida> tareasCumplidasNo = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurnoAndCumplida(ubicacion, fecha, turno, false);
        List<TareaCumplida> tareasCumplidasSi = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurnoAndCumplida(ubicacion, fecha, turno, true);

        model.addAttribute("worker", worker);
        model.addAttribute("ubicacion", ubicacion);
        model.addAttribute("turnoInformado", turno);
        model.addAttribute("tareasCumplidasNo", tareasCumplidasNo);
        model.addAttribute("tareasCumplidasSi", tareasCumplidasSi);
        TareaCumplida tareaCumplida = new TareaCumplida();
        model.addAttribute("tareaCumplida", tareaCumplida);
        TareaCumplidaListWrapper tareaCumplidaListWrapper = new TareaCumplidaListWrapper();
        tareaCumplidaListWrapper.setTareasCumplidas(tareasCumplidasNo);
        model.addAttribute("tareaCumplidaListWrapper", tareaCumplidaListWrapper);


        return "workers/workerstareas";
    }

    @PostMapping("/ubicaciones/{ubicacionId}/tareas")
    public String updateTareas(@PathVariable Long sociedadId, @PathVariable Long ubicacionId, @RequestParam Turno turno, @ModelAttribute("tareaCumplidaListWrapper") TareaCumplidaListWrapper tareaCumplidaListWrapper, RedirectAttributes redirectAttributes) {
        List<TareaCumplida> tareasCumplidasNo = tareaCumplidaListWrapper.getTareasCumplidas();

        logger.info("TareasCumplidasNo: {}", tareasCumplidasNo);

        for (TareaCumplida tareaCumplida : tareasCumplidasNo) {
            logger.info("TareaCumplida ID: {}", tareaCumplida.getId());
            tareaCumplidaService.updateTareaCumplida(tareaCumplida.getId(), tareaCumplida);
        }

        redirectAttributes.addAttribute("turno", turno);
        return "redirect:/worker/" + sociedadId + "/ubicaciones/" + ubicacionId + "/tareas";
    }
    @GetMapping("/password")
    public String showChangePasswordForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("worker", workerService.findByName(userDetails.getUsername()));
        return "workers/cambiar-password";
    }

    @PostMapping("/password")
    public String cambiarPassword(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String confirmNewPassword, RedirectAttributes redirectAttributes) {
        Worker worker = workerService.findByName(userDetails.getUsername());

        if (!newPassword.equals(confirmNewPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Las contraseñas nuevas no coinciden");
            return "redirect:/worker/" + worker.getSociedad().getId() + "/password";
        }

        try {
            workerService.cambiarPassword(worker.getId(), oldPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Contraseña cambiada exitosamente");
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getReason());
        }
        return "redirect:/worker/" + worker.getSociedad().getId() + "/workersmenu";
    }


    @GetMapping("/workersmenu")
    public String workerMenu(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("worker", workerService.findByName(userDetails.getUsername()));
        return "workers/workersmenu";
    }

}
