package MultiAplicacion.services.interfaces;

import MultiAplicacion.DTOs.TareaDTO;
import MultiAplicacion.DTOs.UbicacionDTO;
import MultiAplicacion.entities.Sociedad;
import MultiAplicacion.entities.Tarea;
import MultiAplicacion.entities.Ubicacion;

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
    Ubicacion updateUbicacion(Long id, UbicacionDTO ubicacionDTO);
    List<Ubicacion> findAllAvailableForTarea(Long tareaId);
    List<Ubicacion> findAllBySociedad(Sociedad sociedad);
    List<Ubicacion> findAllBySociedadOrderedById(Sociedad sociedad);
}

