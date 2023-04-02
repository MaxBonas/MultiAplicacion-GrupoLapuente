package Lapuente.TareasUbicaciones.controllers.interfaces;

import Lapuente.TareasUbicaciones.DTOs.*;
import Lapuente.TareasUbicaciones.entities.Tarea;
import Lapuente.TareasUbicaciones.entities.TareaCumplida;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.entities.Worker;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface AdminControllerInterface {

    Worker addWorker(WorkerDTO workerDTO);

    List<Worker> getAllWorkers();

    List<Tarea> getAllTareas();

    Tarea addTarea(TareaDTO tareaDTO);

    List<Ubicacion> getAllUbicaciones();

    Ubicacion addUbicacion(UbicacionDTO ubicacionDTO);

    TareaCumplidaDTO updateTareaCumplida(Long tareaCumplidaId, TareaCumplidaDTO tareaCumplidaDTO);

    List<TareaCumplida> getTareasCumplidasByUbicacionYPeriodo(Long ubicacionId, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    Ubicacion updateTareasDeUbicacion(Long ubicacionId, Set<TareaDTO> tareasDTO);

}
