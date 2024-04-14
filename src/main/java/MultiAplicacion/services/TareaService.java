package MultiAplicacion.services;

import MultiAplicacion.DTOs.TareaDTO;
import MultiAplicacion.entities.Tarea;
import MultiAplicacion.entities.TareaCumplida;
import MultiAplicacion.entities.Ubicacion;
import MultiAplicacion.repositories.TareaCumplidaRepository;
import MultiAplicacion.repositories.TareaRepository;
import MultiAplicacion.repositories.UbicacionRepository;
import MultiAplicacion.repositories.UbicacionTareaRepository;
import MultiAplicacion.services.interfaces.TareaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TareaService implements TareaServiceInterface {

    @Autowired
    private TareaRepository tareaRepository;
    @Autowired
    private UbicacionRepository ubicacionRepository;
    @Autowired
    private TareaCumplidaRepository tareaCumplidaRepository;
    @Autowired
    private UbicacionTareaRepository ubicacionTareaRepository;
    @Autowired
    private UbicacionTareaService ubicacionTareaService;
    @Override
    public List<Tarea> getAllTareas() {
        return tareaRepository.findAll();
    }

    @Override
    public Tarea getTareaById(Long id) {
        return tareaRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe la tarea con id: " + id + " en la base de datos"));
    }

    @Override
    public Tarea saveTarea(TareaDTO tareaDTO) {
        Tarea tarea = new Tarea(tareaDTO.getName(), tareaDTO.getDescripcion());
        return tareaRepository.save(tarea);
    }

    @Override
    public Tarea updateTarea(Long id, TareaDTO tareaDTO) {
        Tarea tarea = tareaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Tarea not found"));
        tarea.setName(tareaDTO.getName());
        tarea.setDescripcion(tareaDTO.getDescripcion());
        return tareaRepository.save(tarea);
    }

    @Override
    @Transactional
    public void deleteTareaById(Long id) {
        Tarea tarea = tareaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Tarea not found"));
        // Marca como borradas las relaciones con las ubicaciones
        tarea.getUbicaciones().forEach(ubicacionTarea -> {
            ubicacionTareaService.deleteByUbicacionIdAndTareaId(ubicacionTarea.getUbicacion().getId(), id);
        });
        // Marca la tarea como borrada
        tarea.setDeleted(true);
        tareaRepository.save(tarea);
    }
    @Override
    public List<Tarea> getTareasByUbicacion(Long id) {
        Ubicacion ubicacion = ubicacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Ubicacion not found"));
        return tareaRepository.findByUbicaciones(ubicacion);
    }
    public List<Tarea> findAllDistinctByName() {
        return tareaRepository.findAllDistinctByName();
    }
    public List<Tarea> findAllOrderedById() {
        return tareaRepository.findAllByOrderById();
    }

}

