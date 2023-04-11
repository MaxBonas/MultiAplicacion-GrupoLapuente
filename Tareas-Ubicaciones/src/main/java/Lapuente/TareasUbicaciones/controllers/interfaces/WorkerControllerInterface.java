package Lapuente.TareasUbicaciones.controllers.interfaces;

import Lapuente.TareasUbicaciones.ENUMs.Turno;
import Lapuente.TareasUbicaciones.entities.Tarea;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.entities.Worker;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface WorkerControllerInterface {
    String getAllUbicaciones(Model model);
    String getTareasByUbicacion(Long ubicacionId, Turno turno, Model model);
    String informarTareasCumplidas(Long ubicacionId, List<Long> tareasCumplidasIds, Turno turno, UserDetails userDetails, String comentario, RedirectAttributes redirectAttributes);
    void cambiarPassword(Long workerId, String oldPassword, String newPassword);
    String selectTurno(Long ubicacionId, Model model);
}

