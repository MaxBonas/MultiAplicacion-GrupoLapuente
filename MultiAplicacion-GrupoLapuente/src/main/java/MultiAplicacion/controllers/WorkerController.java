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
import org.springframework.http.HttpStatus;
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
    public String getAllUbicaciones(Model model, @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long sociedadId) {
        Worker worker = workerService.findByName(userDetails.getUsername());
        Sociedad workerSociedad = worker.getSociedad();

        // Añadir validación de sociedad
        if (!workerSociedad.getId().equals(sociedadId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
        }

        List<Ubicacion> ubicaciones = ubicacionService.findAllBySociedad(workerSociedad);
        model.addAttribute("worker", worker);
        model.addAttribute("ubicaciones", ubicaciones);
        return "workers/workersubicaciones";
    }

    @GetMapping("/ubicaciones/{ubicacionId}/selectturno")
    public String selectTurno(@PathVariable Long sociedadId, @PathVariable Long ubicacionId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Worker worker = workerService.findByName(userDetails.getUsername());
        // Añadir validación de sociedad
        if (!worker.getSociedad().getId().equals(sociedadId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
        }
        model.addAttribute("worker", worker);
        model.addAttribute("ubicacionId", ubicacionId);

        return "workers/workersturno";
    }

    @GetMapping("/ubicaciones/{ubicacionId}/tareas")
    public String showTareas(@PathVariable Long sociedadId, @PathVariable Long ubicacionId, @RequestParam Turno turno, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Worker worker = workerService.findByName(userDetails.getUsername());
        // Añadir validación de sociedad
        if (!worker.getSociedad().getId().equals(sociedadId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
        }
        Ubicacion ubicacion = ubicacionService.findById(ubicacionId);
        LocalDateTime fecha = LocalDateTime.now();

        List<TareaCumplida> tareasCumplidasSi = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurnoAndCumplida(ubicacion, fecha, turno, true);

        // Verifica si hay tareas cumplidas antes de crear tareas cumplidas vacías
        if (tareasCumplidasSi.isEmpty()) {
            workerService.crearTareasCumplidasVacias(worker.getId(), ubicacionId, fecha, turno);
        }

        List<TareaCumplida> tareasCumplidasNo = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurnoAndCumplida(ubicacion, fecha, turno, false);

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
    public String updateTareas(@PathVariable Long sociedadId, @PathVariable Long ubicacionId, @RequestParam Turno turno,
                               @ModelAttribute("tareaCumplidaListWrapper") TareaCumplidaListWrapper tareaCumplidaListWrapper,
                               RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails) {
        List<TareaCumplida> tareasCumplidasNo = tareaCumplidaListWrapper.getTareasCumplidas();

        logger.info("TareasCumplidasNo: {}", tareasCumplidasNo);

        Worker currentWorker = workerService.findByName(userDetails.getUsername());
        // Añadir validación de sociedad
        if (!currentWorker.getSociedad().getId().equals(sociedadId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
        }
        for (TareaCumplida tareaCumplida : tareasCumplidasNo) {
            logger.info("TareaCumplida ID: {}", tareaCumplida.getId());
            tareaCumplida.setWorker(currentWorker);
            logger.info("TareaCumplida Worker: {}", tareaCumplida.getWorker());
            tareaCumplidaService.updateTareaCumplida(tareaCumplida.getId(), tareaCumplida);
        }

        redirectAttributes.addAttribute("turno", turno);
        return "redirect:/worker/" + sociedadId + "/ubicaciones/" + ubicacionId + "/tareas";
    }

    @GetMapping("/password")
    public String showChangePasswordForm(@PathVariable Long sociedadId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Worker worker = workerService.findByName(userDetails.getUsername());
        // Añadir validación de sociedad
        if (!worker.getSociedad().getId().equals(sociedadId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
        }
        model.addAttribute("worker", worker);
        return "workers/cambiar-password";
    }


    @PostMapping("/password")
    public String cambiarPassword(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String confirmNewPassword, RedirectAttributes redirectAttributes) {
        Worker worker = workerService.findByName(userDetails.getUsername());

        // Agrega la validación para entradas vacías
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Los campos de contraseña no pueden estar vacíos");
            return "redirect:/worker/" + worker.getSociedad().getId() + "/password";
        }

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
    public String workerMenu(@PathVariable Long sociedadId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Worker worker = workerService.findByName(userDetails.getUsername());
        // Añadir validación de sociedad
        if (!worker.getSociedad().getId().equals(sociedadId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
        }
        model.addAttribute("worker", worker);
        return "workers/workersmenu";
    }

}
