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
// Esta anotación define que esta clase es un controlador en el marco Spring MVC.
// La anotación @RequestMapping establece la ruta base para todas las rutas manejadas por este controlador.
@Controller
@RequestMapping("/worker/{sociedadId}")
public class WorkerController implements WorkerControllerInterface {

    // Logger para registrar eventos en este controlador
    private final Logger logger = LoggerFactory.getLogger(WorkerController.class);

    // Inyección de dependencias de varios servicios y repositorios que serán usados en este controlador
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

    // Este método gestiona las solicitudes GET a la ruta /worker/{sociedadId}/ubicaciones
    // Retorna la vista de todas las ubicaciones disponibles para un trabajador en una sociedad específica.
    @GetMapping("/ubicaciones")
    public String getAllUbicaciones(Model model, @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long sociedadId) {
        Worker worker = workerService.findByName(userDetails.getUsername());
        Sociedad workerSociedad = worker.getSociedad();

        // Si el ID de la sociedad del trabajador no coincide con el ID de la sociedad en la ruta, se lanza una excepción
        if (!workerSociedad.getId().equals(sociedadId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
        }

        List<Ubicacion> ubicaciones = ubicacionService.findAllBySociedad(workerSociedad);
        model.addAttribute("worker", worker);
        model.addAttribute("ubicaciones", ubicaciones);
        return "workers/workersubicaciones";
    }

    // Este método gestiona las solicitudes GET a la ruta /worker/{sociedadId}/ubicaciones/{ubicacionId}/selectturno
    // Retorna la vista donde un trabajador puede seleccionar su turno en una ubicación específica.
    @GetMapping("/ubicaciones/{ubicacionId}/selectturno")
    public String selectTurno(@PathVariable Long sociedadId, @PathVariable Long ubicacionId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Worker worker = workerService.findByName(userDetails.getUsername());

        // Si el ID de la sociedad del trabajador no coincide con el ID de la sociedad en la ruta, se lanza una excepción
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
    // Este método gestiona las solicitudes GET a la ruta /worker/{sociedadId}/ubicaciones/{ubicacionId}/tareas
// Retorna la vista de todas las tareas disponibles para un trabajador en una ubicación y turno específicos.
    @GetMapping("/ubicaciones/{ubicacionId}/tareas")
    public String showTareas(@PathVariable Long sociedadId, @PathVariable Long ubicacionId, @RequestParam Turno turno, @RequestParam List<String> workers, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Worker worker = workerService.findByName(userDetails.getUsername());

        // Si el ID de la sociedad del trabajador no coincide con el ID de la sociedad en la ruta, se lanza una excepción
        if (!worker.getSociedad().getId().equals(sociedadId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
        }

        Ubicacion ubicacion = ubicacionService.findById(ubicacionId);
        LocalDateTime fecha = LocalDateTime.now();

        // Buscar las tareas que ya se han cumplido
        List<TareaCumplida> tareasCumplidasSi = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurnoAndCumplida(ubicacion, fecha, turno, true);

        // Si no hay tareas cumplidas, crear tareas cumplidas vacías
        if (tareasCumplidasSi.isEmpty()) {
            workerService.crearTareasCumplidasVacias(worker.getId(), ubicacionId, fecha, turno, String.join(", ", workers));
        }

        // Buscar las tareas que aún no se han cumplido
        List<TareaCumplida> tareasCumplidasNo = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurnoAndCumplida(ubicacion, fecha, turno, false);

        // Agregar datos al modelo para ser usados en la vista
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

    // Este método gestiona las solicitudes POST a la ruta /worker/{sociedadId}/ubicaciones/{ubicacionId}/tareas
// Actualiza las tareas cumplidas y no cumplidas de un trabajador en una ubicación y turno específicos.
    @PostMapping("/ubicaciones/{ubicacionId}/tareas")
    public String updateTareas(@PathVariable Long sociedadId, @PathVariable Long ubicacionId, @RequestParam Turno turno, @RequestParam String workers,
                               @ModelAttribute("tareaCumplidaListWrapper") TareaCumplidaListWrapper tareaCumplidaListWrapper,
                               RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails) {
        List<TareaCumplida> tareasCumplidasNo = tareaCumplidaListWrapper.getTareasCumplidas();

        Worker currentWorker = workerService.findByName(userDetails.getUsername());

        // Si el ID de la sociedad del trabajador no coincide con el ID de la sociedad en la ruta, se lanza una excepción
        if (!currentWorker.getSociedad().getId().equals(sociedadId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
        }

        // Actualiza cada tarea cumplida con la información del trabajador y la actualiza en el servicio
        for (TareaCumplida tareaCumplida : tareasCumplidasNo) {
            tareaCumplida.setWorker(currentWorker);
            tareaCumplidaService.updateTareaCumplida(tareaCumplida.getId(), tareaCumplida);
        }

        // Redirige al trabajador de vuelta a la lista de tareas con los atributos actualizados
        redirectAttributes.addAttribute("turno", turno);
        redirectAttributes.addAttribute("workers", workers);
        return "redirect:/worker/" + sociedadId + "/ubicaciones/" + ubicacionId + "/tareas";
    }

/*
// Este método gestiona las solicitudes GET a la ruta /worker/{sociedadId}/password
// Retorna una vista para que el trabajador pueda cambiar su contraseña.
@GetMapping("/password")
public String showChangePasswordForm(@PathVariable Long sociedadId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
    Worker worker = workerService.findByName(userDetails.getUsername());

    // Si el ID de la sociedad del trabajador no coincide con el ID de la sociedad en la ruta, se lanza una excepción
    if (!worker.getSociedad().getId().equals(sociedadId)) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
    }
    model.addAttribute("worker", worker);
    return "workers/cambiar-password";
}

// Este método gestiona las solicitudes POST a la ruta /worker/{sociedadId}/password
// Actualiza la contraseña del trabajador si se cumplen las condiciones.
@PostMapping("/password")
public String cambiarPassword(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String confirmNewPassword, RedirectAttributes redirectAttributes) {
    Worker worker = workerService.findByName(userDetails.getUsername());

    // Si alguna de las contraseñas está vacía, se redirige al formulario de cambio de contraseña con un mensaje de error
    if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Los campos de contraseña no pueden estar vacíos");
        return "redirect:/worker/" + worker.getSociedad().getId() + "/password";
    }

    // Si las nuevas contraseñas no coinciden, se redirige al formulario de cambio de contraseña con un mensaje de error
    if (!newPassword.equals(confirmNewPassword)) {
        redirectAttributes.addFlashAttribute("errorMessage", "Las contraseñas nuevas no coinciden");
        return "redirect:/worker/" + worker.getSociedad().getId() + "/password";
    }

    // Intenta cambiar la contraseña. Si se lanza una excepción, se redirige al formulario de cambio de contraseña con un mensaje de error
    try {
        workerService.cambiarPassword(worker.getId(), oldPassword, newPassword);
        redirectAttributes.addFlashAttribute("successMessage", "Contraseña cambiada exitosamente");
    } catch (ResponseStatusException e) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getReason());
    }
    return "redirect:/worker/" + worker.getSociedad().getId() + "/workersmenu";
}
*/

    // Este método gestiona las solicitudes GET a la ruta /worker/{sociedadId}/workersmenu
// Retorna una vista del menú principal para los trabajadores.
    @GetMapping("/workersmenu")
    public String workerMenu(@PathVariable Long sociedadId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Worker worker = workerService.findByName(userDetails.getUsername());

        // Si el ID de la sociedad del trabajador no coincide con el ID de la sociedad en la ruta, se lanza una excepción
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
