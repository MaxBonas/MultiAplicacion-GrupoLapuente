package Lapuente.TareasUbicaciones.services.interfaces;

import Lapuente.TareasUbicaciones.entities.Tarea;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.entities.Worker;

import java.util.List;

public interface TareaServiceInterface {

    List<Tarea> getAllTareas();

    Tarea getTareaById(Long id);

    Tarea saveTarea(Tarea tarea);

    void deleteTareaById(Long id);

    List<Tarea> getTareasByWorker(Worker worker);


    List<Tarea> getTareasByUbicacion(Long id);

    void confirmarTarea(Long tareaId, Long ubicacionId, boolean tareaCumplida);
}
