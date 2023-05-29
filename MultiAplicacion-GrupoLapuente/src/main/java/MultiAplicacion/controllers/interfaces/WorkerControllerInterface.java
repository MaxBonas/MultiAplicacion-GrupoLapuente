package MultiAplicacion.controllers.interfaces;

import MultiAplicacion.DTOs.MensajeDTO;
import MultiAplicacion.DTOs.TareaCumplidaDTO;
import MultiAplicacion.DTOs.TareaCumplidaListWrapper;
import MultiAplicacion.ENUMs.Turno;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

public interface WorkerControllerInterface {
    String getAllUbicaciones(Model model, @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long sociedadId);
    //String cambiarPassword(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String confirmNewPassword, RedirectAttributes redirectAttributes);
    String selectTurno(@PathVariable Long sociedadId, @PathVariable Long ubicacionId, Model model, @AuthenticationPrincipal UserDetails userDetails);
    String workerMenu(@PathVariable Long sociedadId, Model model, @AuthenticationPrincipal UserDetails userDetails);
    //String showChangePasswordForm(@PathVariable Long sociedadId, Model model, @AuthenticationPrincipal UserDetails userDetails);
    public String updateTareas(@PathVariable Long sociedadId, @PathVariable Long ubicacionId, @RequestParam Turno turno,
                               @ModelAttribute("tareaCumplidaListWrapper") TareaCumplidaListWrapper tareaCumplidaListWrapper,
                               RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails);
    String showTareas(@PathVariable Long sociedadId, @PathVariable Long ubicacionId, @RequestParam Turno turno, @RequestParam List<String> workers, Model model, @AuthenticationPrincipal UserDetails userDetails);
    //MIssatges
    String showTablonAnuncios(@PathVariable Long sociedadId, Model model, @AuthenticationPrincipal UserDetails userDetails);
    String showMensaje(@PathVariable Long sociedadId, @PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails);
    String deleteMensaje(@PathVariable Long sociedadId, @PathVariable Long id, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails);

}

