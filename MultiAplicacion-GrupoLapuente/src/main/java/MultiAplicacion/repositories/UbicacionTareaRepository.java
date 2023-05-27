package MultiAplicacion.repositories;

import MultiAplicacion.entities.Ubicacion;
import MultiAplicacion.entities.UbicacionTarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UbicacionTareaRepository extends JpaRepository<UbicacionTarea, Long> {
    UbicacionTarea findByUbicacionIdAndTareaId(Long ubicacionId, Long tareaId);
    List<UbicacionTarea> findAllByTareaIdAndDeletedFalse(Long tareaId);
    List<UbicacionTarea> findAllByUbicacionId(Long ubicacionId);
}
