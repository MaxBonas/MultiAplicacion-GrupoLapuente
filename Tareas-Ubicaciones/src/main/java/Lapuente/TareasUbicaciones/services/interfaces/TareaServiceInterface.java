package Lapuente.TareasUbicaciones.services.interfaces;

import Lapuente.TareasUbicaciones.DTOs.TareaDTO;
import Lapuente.TareasUbicaciones.entities.Tarea;

import java.util.List;

public interface TareaServiceInterface {
    List<Tarea> getAllTareas();
    Tarea getTareaById(Long id);
    Tarea saveTarea(TareaDTO tarea);
    Tarea updateTarea(TareaDTO tarea);
    void deleteTareaById(Long id);
    List<Tarea> getTareasByUbicacion(Long id);
}
