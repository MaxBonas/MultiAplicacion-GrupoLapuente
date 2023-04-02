package Lapuente.TareasUbicaciones.services.interfaces;

import Lapuente.TareasUbicaciones.DTOs.TareaDTO;
import Lapuente.TareasUbicaciones.DTOs.UbicacionDTO;
import Lapuente.TareasUbicaciones.entities.Tarea;
import Lapuente.TareasUbicaciones.entities.Ubicacion;

import java.util.List;
import java.util.Set;

public interface UbicacionServiceInterface {
    List<Ubicacion> findAll();
    Ubicacion findById(Long id);
    Ubicacion save(UbicacionDTO ubicacion);
    void deleteById(Long id);
    List<Ubicacion> findByName(String name);
    List<Tarea> getTareasByUbicacionId(Long id); // Nuevo método

    Ubicacion addTareaAUbicacion(Long ubicacionId, TareaDTO tareaDTO);

    Ubicacion updateTareasDeUbicacion(Long ubicacionId, Set<TareaDTO> tareasDTO);
}

