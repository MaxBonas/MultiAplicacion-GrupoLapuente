package MultiAplicacion.controllers.interfaces;

import MultiAplicacion.DTOs.*;
import MultiAplicacion.ENUMs.Turno;
import MultiAplicacion.entities.TareaCumplida;
import MultiAplicacion.entities.Ubicacion;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface AdminControllerInterface {
    String adminMenu(@PathVariable Long sociedadId, Model model, HttpServletRequest request);
    String cambioSociedad(@PathVariable Long sociedadId, Model model, HttpServletRequest request);
    String cambiarSociedad(@PathVariable Long sociedadId, @RequestParam Long nuevaSociedadId, HttpServletRequest request);
    String getAllWorkers(@PathVariable Long sociedadId, Model model);
    String showCreateWorkerForm(Model model);
    String addWorker(@PathVariable("sociedadId") Long sociedadId, @ModelAttribute WorkerDTO workerDTO, RedirectAttributes redirectAttributes);
    String showEditWorkerForm(@PathVariable Long id, Model model);
    String updateWorker(@PathVariable Long sociedadId, @PathVariable Long id, @ModelAttribute WorkerDTO workerDTO, RedirectAttributes redirectAttributes);
    String getWorker(@PathVariable Long id, Model model);
    String deleteWorker(@PathVariable Long sociedadId, @PathVariable Long id, RedirectAttributes redirectAttributes);
    String changePasswordAdmin(@RequestParam String newPassword, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails currentUser);
    String showChangePasswordForm(@PathVariable Long sociedadId, Model model);
    String showEditTareaForm(@PathVariable Long id, Model model);
    String updateTarea(@PathVariable Long sociedadId, @PathVariable Long id, @ModelAttribute TareaDTO tareaDTO, RedirectAttributes redirectAttributes);
    String deleteTarea(@PathVariable Long sociedadId, @PathVariable Long id, RedirectAttributes redirectAttributes);
    String getTarea(@PathVariable Long id, Model model);
    String getAllUbicaciones(@PathVariable("sociedadId") Long sociedadId, Model model);
    String addUbicacion(@PathVariable("sociedadId") Long sociedadId, @ModelAttribute UbicacionDTO ubicacionDTO, RedirectAttributes redirectAttributes);
    String updateUbicacion(@PathVariable Long sociedadId, @PathVariable Long id, @ModelAttribute UbicacionDTO ubicacionDTO, RedirectAttributes redirectAttributes);
    String getUbicacion(@PathVariable Long id, Model model);
    String deleteUbicacion(@PathVariable Long sociedadId, @PathVariable Long id, RedirectAttributes redirectAttributes);
    String showAssignTareaForm(@RequestParam Long ubicacionId, Model model);
    String createAndAssignTarea(@RequestParam Long ubicacionId, @RequestParam Long tareaId, @RequestParam(required = false) String name, @RequestParam(required = false) String descripcion, RedirectAttributes redirectAttributes);
    String informeDiarioRequest(@PathVariable("sociedadId") Long sociedadId, Model model);
    String informeDiario(@PathVariable("sociedadId") Long sociedadId, @RequestParam("fecha") String fechaStr, Model model) ;
//Missatges:
    String showTablonAnuncios(@PathVariable Long sociedadId, Model model, HttpServletRequest request);
    String showCrearMensaje(@PathVariable Long sociedadId, Model model, HttpServletRequest request);
    String sendMensaje(@PathVariable Long sociedadId, @ModelAttribute MensajeDTO mensajeDTO, RedirectAttributes redirectAttributes, Authentication authentication);
    String showMensaje(@PathVariable Long sociedadId, @PathVariable Long id, Model model, HttpServletRequest request);
    String deleteMensaje(@PathVariable Long sociedadId, @PathVariable Long id, RedirectAttributes redirectAttributes);
    String toggleMensajeActivo(@PathVariable Long sociedadId, @PathVariable Long id, RedirectAttributes redirectAttributes);
}
