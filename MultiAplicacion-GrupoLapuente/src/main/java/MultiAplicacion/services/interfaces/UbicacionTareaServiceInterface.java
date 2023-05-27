package MultiAplicacion.services.interfaces;

import MultiAplicacion.entities.UbicacionTarea;

import java.util.List;

public interface UbicacionTareaServiceInterface {
    void deleteByUbicacionIdAndTareaId(Long ubicacionId, Long tareaId);
    List<UbicacionTarea> getAllByUbicacionId(Long ubicacionId);
    List<UbicacionTarea> getAllByTareaId(Long tareaId);
}
