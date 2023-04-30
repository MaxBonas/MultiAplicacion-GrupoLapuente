package MultiAplicacion.controllers.interfaces;

import MultiAplicacion.DTOs.*;
import MultiAplicacion.ENUMs.Turno;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.YearMonth;

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
    String informeDiario(@RequestParam("fecha") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha, Model model);
}
