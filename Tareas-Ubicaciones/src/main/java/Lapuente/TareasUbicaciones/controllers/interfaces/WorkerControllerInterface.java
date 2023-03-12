package Lapuente.TareasUbicaciones.controllers.interfaces;

import Lapuente.TareasUbicaciones.entities.Tarea;
import Lapuente.TareasUbicaciones.entities.TareasDiarias;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.entities.Worker;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface WorkerControllerInterface {

    List<Ubicacion> getAllUbicaciones();

    List<Tarea> getTareasByUbicacion(Long ubicacionId);

    Tarea getTareaById(Long tareaId);

    void confirmarTarea(Long tareaId, Long ubicacionId, ConfirmacionTarea confirmacionTarea);

    void asignarTareas(Long ubicacionId, Long tareaId);

    List<TareasDiarias> getTareasDiarias();

    void cambiarPassword(CambioPassword cambioPassword);

    void agregarComentariosInforme(ComentariosInforme comentariosInforme);

    List<TareasDiarias> getTareasDiariasSiguientes(LocalDate fecha);

}
