package MultiAplicacion.services;

import MultiAplicacion.entities.Ubicacion;
import MultiAplicacion.entities.UbicacionTarea;
import MultiAplicacion.repositories.UbicacionTareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UbicacionTareaService {

    @Autowired
    private UbicacionTareaRepository ubicacionTareaRepository;

    @Transactional
    public void deleteByUbicacionIdAndTareaId(Long ubicacionId, Long tareaId) {
        UbicacionTarea ubicacionTarea = ubicacionTareaRepository.findByUbicacionIdAndTareaId(ubicacionId, tareaId);
        if (ubicacionTarea != null) {
            ubicacionTarea.setDeleted(true);
            ubicacionTareaRepository.save(ubicacionTarea);
        }
    }

    public List<UbicacionTarea> getAllByUbicacionId(Long ubicacionId) {
        return ubicacionTareaRepository.findAllByUbicacionId(ubicacionId);
    }

    public List<UbicacionTarea> getAllByTareaId(Long tareaId) {
        return ubicacionTareaRepository.findAllByTareaIdAndDeletedFalse(tareaId);
    }
    @Transactional
    public void deleteByUbicacion(Ubicacion ubicacion) {
        List<UbicacionTarea> ubicacionTareas = getAllByUbicacionId(ubicacion.getId());
        for (UbicacionTarea ubicacionTarea : ubicacionTareas) {
            ubicacionTarea.setDeleted(true);
            ubicacionTareaRepository.save(ubicacionTarea);
        }
    }
}
