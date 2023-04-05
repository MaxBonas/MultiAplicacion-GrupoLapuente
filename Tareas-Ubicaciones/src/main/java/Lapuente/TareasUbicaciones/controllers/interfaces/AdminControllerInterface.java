package Lapuente.TareasUbicaciones.controllers.interfaces;

import Lapuente.TareasUbicaciones.DTOs.*;
import Lapuente.TareasUbicaciones.ENUMs.Turno;
import Lapuente.TareasUbicaciones.entities.Tarea;
import Lapuente.TareasUbicaciones.entities.TareaCumplida;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.entities.Worker;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;

public interface AdminControllerInterface {
    String addWorker(WorkerDTO workerDTO, RedirectAttributes redirectAttributes);
    String getAllWorkers(Model model);
    String getAllTareas(Model model);
    String addTarea(TareaDTO tareaDTO, RedirectAttributes redirectAttributes);
    String getAllUbicaciones(Model model);
    String addUbicacion(UbicacionDTO ubicacionDTO, RedirectAttributes redirectAttributes);
    String updateWorker(Long id, WorkerDTO workerDTO, RedirectAttributes redirectAttributes);
    String updateTarea(Long id, TareaDTO tareaDTO, RedirectAttributes redirectAttributes);
    String updateUbicacion(Long id, UbicacionDTO ubicacionDTO, RedirectAttributes redirectAttributes);
    String deleteWorker(Long id, RedirectAttributes redirectAttributes);
    String deleteTarea(Long id, RedirectAttributes redirectAttributes);
    String deleteUbicacion(Long id, RedirectAttributes redirectAttributes);
    String getWorker(Long id, Model model);
    String getTarea(Long id, Model model);
    String getUbicacion(Long id, Model model);
    String addTareaAUbicacion(Long ubicacionId, TareaDTO tareaDTO, RedirectAttributes redirectAttributes);
    String updateTareasDeUbicacion(Long ubicacionId, Set<TareaDTO> tareasDTO, RedirectAttributes redirectAttributes);
    String getInformeDiario(LocalDate fecha, Model model);
    String getInformeTurnoUbicacion(LocalDate fecha, Turno turno, Long ubicacionId, Model model);
    String getInformeMensual(YearMonth mes, Model model);
}

