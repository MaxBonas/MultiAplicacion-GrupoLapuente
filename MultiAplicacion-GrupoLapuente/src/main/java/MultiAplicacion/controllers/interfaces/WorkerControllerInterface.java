package MultiAplicacion.controllers.interfaces;

import MultiAplicacion.ENUMs.Turno;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

public interface WorkerControllerInterface {
    String getAllUbicaciones(Model model, @AuthenticationPrincipal UserDetails userDetails);
    String getTareasByUbicacion(Long ubicacionId, Turno turno, Model model);
    String informarTareasCumplidas(Long ubicacionId, List<Long> tareasCumplidasIds, Turno turno, UserDetails userDetails, String comentario, RedirectAttributes redirectAttributes);
    void cambiarPassword(Long workerId, String oldPassword, String newPassword);
    String selectTurno(@PathVariable Long ubicacionId, Model model, @AuthenticationPrincipal UserDetails userDetails);
    String workerMenu(Model model, @AuthenticationPrincipal UserDetails userDetails);
}

