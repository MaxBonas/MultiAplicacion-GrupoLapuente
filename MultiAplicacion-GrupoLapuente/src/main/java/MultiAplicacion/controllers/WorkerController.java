package MultiAplicacion.controllers;

import MultiAplicacion.DTOs.MensajeDTO;
import MultiAplicacion.DTOs.TareaCumplidaDTO;
import MultiAplicacion.DTOs.TareaCumplidaListWrapper;
import MultiAplicacion.ENUMs.Turno;
import MultiAplicacion.controllers.interfaces.WorkerControllerInterface;
import MultiAplicacion.entities.*;
import MultiAplicacion.repositories.TareaCumplidaRepository;
import MultiAplicacion.repositories.UbicacionRepository;
import MultiAplicacion.repositories.UserRepository;
import MultiAplicacion.repositories.WorkerRepository;
import MultiAplicacion.services.MensajeService;
import MultiAplicacion.services.TareaCumplidaService;
import MultiAplicacion.services.interfaces.UbicacionServiceInterface;
import MultiAplicacion.services.interfaces.WorkerServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Autowired
    private MensajeService mensajeService;
    @Autowired
    private UserRepository userRepository;

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
        // Agregar todos los trabajadores de la sociedad al modelo
        List<Worker> allWorkers = workerService.getAllWorkers();
        allWorkers.sort(Comparator.comparing(Worker::getName));
        model.addAttribute("allWorkers", allWorkers);
        return "workers/workersturno";
    }

    @GetMapping("/ubicaciones/{ubicacionId}/tareas")
    public String showTareas(@PathVariable Long sociedadId, @PathVariable Long ubicacionId, @RequestParam Turno turno, @RequestParam List<String> workers, Model model, @AuthenticationPrincipal UserDetails userDetails) {
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
            workerService.crearTareasCumplidasVacias(worker.getId(), ubicacionId, fecha, turno, String.join(", ", workers));
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
        model.addAttribute("workersTurno", String.join(", ", workers));

        return "workers/workerstareas";
    }

    @PostMapping("/ubicaciones/{ubicacionId}/tareas")
    public String updateTareas(@PathVariable Long sociedadId, @PathVariable Long ubicacionId, @RequestParam Turno turno, @RequestParam String workers,
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
        redirectAttributes.addAttribute("workers", workers);
        return "redirect:/worker/" + sociedadId + "/ubicaciones/" + ubicacionId + "/tareas";
    }

/*
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

*/
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

    // Métodos de mensajes
    @GetMapping("/tablonanuncios")
    public String showTablonAnuncios(@PathVariable Long sociedadId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Mensaje> mensajesCirculares = mensajeService.findMensajesCirculares();
        User worker = userRepository.findByName(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Mensaje> mensajesRecibidos = mensajeService.findMensajesByReceptor(worker);
        List<Mensaje> mensajes = Stream.concat(mensajesCirculares.stream(), mensajesRecibidos.stream()).collect(Collectors.toList());
        model.addAttribute("mensajes", mensajes);
        return "workers/tablonanuncios";
    }

    @GetMapping("/mensajes/{id}")
    public String showMensaje(@PathVariable Long sociedadId, @PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Mensaje> optionalMensaje = mensajeService.findMensajeById(id);
        if (optionalMensaje.isPresent()) {
            Mensaje mensaje = optionalMensaje.get();
            model.addAttribute("mensaje", mensaje);
            return "workers/mensaje";
        } else {
            return "redirect:/worker/{sociedadId}/tablonanuncios?error=El mensaje no se encontró";
        }
    }

    @DeleteMapping("/mensajes/{id}")
    public String deleteMensaje(@PathVariable Long sociedadId, @PathVariable Long id, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Mensaje> optionalMensaje = mensajeService.findMensajeById(id);
        if (optionalMensaje.isPresent()) {
            Mensaje mensaje = optionalMensaje.get();
            User worker = userRepository.findByName(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (mensaje.getReceptor() != null && mensaje.getReceptor().equals(worker)) {
                mensajeService.deleteMensaje(id);
                redirectAttributes.addFlashAttribute("success", "Mensaje eliminado con éxito");
            } else {
                redirectAttributes.addFlashAttribute("error", "No tiene permiso para eliminar este mensaje");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Mensaje no encontrado");
        }
        return "redirect:/worker/{sociedadId}/tablonanuncios";
    }

}
