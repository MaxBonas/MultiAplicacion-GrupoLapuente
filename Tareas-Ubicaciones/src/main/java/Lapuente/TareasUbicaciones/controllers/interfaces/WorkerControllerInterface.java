package Lapuente.TareasUbicaciones.controllers.interfaces;

import Lapuente.TareasUbicaciones.ENUMs.Turno;
import Lapuente.TareasUbicaciones.entities.Tarea;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.entities.Worker;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface WorkerControllerInterface {

    List<Ubicacion> getAllUbicaciones();

    List<Tarea> getTareasByUbicacion(Long ubicacionId);

    void informarTareasCumplidas(Long ubicacionId, Turno turno, List<Long> tareasCumplidasIds, UserDetails userDetails, String comentario);

    void cambiarPassword(Long workerId, String oldPassword, String newPassword);

}

