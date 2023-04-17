package MultiAplicacion.controllers.interfaces;

import MultiAplicacion.DTOs.*;
import MultiAplicacion.ENUMs.Turno;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.YearMonth;

public interface AdminControllerInterface {
    String adminMenu(@PathVariable Long sociedadId, Model model, HttpServletRequest request);
    String addWorker(WorkerDTO workerDTO, RedirectAttributes redirectAttributes);
    String getAllWorkers(@PathVariable Long sociedadId, Model model);
    String getAllTareas(Model model);
    String addTarea(TareaDTO tareaDTO, RedirectAttributes redirectAttributes);
    String getAllUbicaciones(Model model);
    String addUbicacion(UbicacionDTO ubicacionDTO, RedirectAttributes redirectAttributes);
    String getWorker(Long id, Model model);
    String getTarea(Long id, Model model);
    String getUbicacion(Long id, Model model);
    String addTareaAUbicacion(Long ubicacionId, TareaDTO tareaDTO, RedirectAttributes redirectAttributes);
    ResponseEntity<byte[]> getInformeDiario(LocalDate fecha);
    String getInformeTurnoUbicacion(LocalDate fecha, Turno turno, Long ubicacionId, Model model);
    String getInformeMensual(YearMonth mes, Model model);
    String getInformeDiarioRequest(Model model);
    String showCreateWorkerForm(Model model);
    String showCreateTareaForm(Model model);
    String showAssignTareaForm(@PathVariable Long tareaId, Model model);
    String assignTareaToUbicacion(@PathVariable Long tareaId, @RequestParam Long ubicacionId, RedirectAttributes redirectAttributes);
    String showEditTareaForm(@PathVariable Long id, Model model);
    String cambioSociedad(@PathVariable Long sociedadId, Model model, HttpServletRequest request);
    String cambiarSociedad(@PathVariable Long sociedadId, @RequestParam Long nuevaSociedadId, HttpServletRequest request);

}

