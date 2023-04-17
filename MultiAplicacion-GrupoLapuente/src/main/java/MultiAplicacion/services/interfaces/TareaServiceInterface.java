package MultiAplicacion.services.interfaces;

import MultiAplicacion.DTOs.TareaDTO;
import MultiAplicacion.entities.Tarea;

import java.util.List;

public interface TareaServiceInterface {
    List<Tarea> getAllTareas();
    Tarea getTareaById(Long id);
    Tarea saveTarea(TareaDTO tarea);
    Tarea updateTarea(Long id, TareaDTO tarea);
    void deleteTareaById(Long id);
    List<Tarea> getTareasByUbicacion(Long id);
}
