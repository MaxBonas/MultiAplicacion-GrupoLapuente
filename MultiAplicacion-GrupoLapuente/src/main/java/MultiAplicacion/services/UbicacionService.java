package MultiAplicacion.services;

import MultiAplicacion.DTOs.TareaDTO;
import MultiAplicacion.DTOs.UbicacionDTO;
import MultiAplicacion.entities.Sociedad;
import MultiAplicacion.entities.Tarea;
import MultiAplicacion.entities.Ubicacion;
import MultiAplicacion.repositories.SociedadRepository;
import MultiAplicacion.repositories.TareaRepository;
import MultiAplicacion.repositories.UbicacionRepository;
import MultiAplicacion.services.interfaces.UbicacionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UbicacionService implements UbicacionServiceInterface {

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private TareaRepository tareaRepository; // Inyectar el repositorio de Tarea
    @Autowired
    private SociedadRepository sociedadRepository;

    @Override
    public List<Ubicacion> findAll() {
        return ubicacionRepository.findAll();
    }

    @Override
    public Ubicacion findById(Long id) {
        return ubicacionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "No existe la ubicacion con id: " + id + " en la base de datos"));
    }
    @Override
    public Ubicacion save(UbicacionDTO ubicacionDTO) {
        // Convertir el objeto UbicacionDTO a Ubicacion
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setName(ubicacionDTO.getName());
        ubicacion.setTareas(new HashSet<>());
        ubicacion.setSociedad(sociedadRepository.findById(ubicacionDTO.getSociedadId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe la sociedad con id: " + ubicacionDTO.getSociedadId() + " en la base de datos")));

        // Guardar el objeto Ubicacion en el repositorio
        return ubicacionRepository.save(ubicacion);
    }
    @Override
    public List<Ubicacion> findByName(String name) {
        return ubicacionRepository.findByName(name);
    }

    @Override
    public List<Ubicacion> findAllBySociedad(Sociedad sociedad) {
        return ubicacionRepository.findBySociedad(sociedad);
    }
    @Override
    public void deleteById(Long id) {
        ubicacionRepository.deleteById(id);
    }

    @Override
    public List<Tarea> getTareasByUbicacionId(Long id) {
        Ubicacion ubicacion = ubicacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Ubicacion not found"));
        return tareaRepository.findByUbicaciones(ubicacion);
    }
    @Override
    public Ubicacion addTareaAUbicacion(Long ubicacionId, TareaDTO tareaDTO) {
        Ubicacion ubicacion = findById(ubicacionId);
        Tarea tarea = new Tarea(tareaDTO.getName(), tareaDTO.getDescripcion());
        tareaRepository.save(tarea);
        ubicacion.getTareas().add(tarea);
        tarea.getUbicaciones().add(ubicacion); // Agregar la ubicación a la tarea
        return ubicacionRepository.save(ubicacion);
    }
    @Override
    public Ubicacion updateTareasDeUbicacion(Long ubicacionId, Set<TareaDTO> tareasDTO) {
        Ubicacion ubicacion = findById(ubicacionId);
        Set<Tarea> tareas = tareasDTO.stream()
                .map(tareaDTO -> {
                    Tarea tarea = new Tarea(tareaDTO.getName(), tareaDTO.getDescripcion());
                    tarea.getUbicaciones().add(ubicacion); // Agregar la ubicación a cada tarea
                    return tarea;
                })
                .collect(Collectors.toSet());
        ubicacion.setTareas(tareas);
        tareaRepository.saveAll(tareas);
        return ubicacionRepository.save(ubicacion);
    }

    @Override
    public Ubicacion updateUbicacion(Long id, UbicacionDTO ubicacionDTO) {
        Ubicacion ubicacion = new Ubicacion(ubicacionDTO.getName(), sociedadRepository.getById(ubicacionDTO.getSociedadId()));
        ubicacion.setId(id);
        return ubicacionRepository.save(ubicacion);
    }

    public List<Ubicacion> findAllAvailableForTarea(Long tareaId) {
        // Obtén todas las ubicaciones
        List<Ubicacion> allUbicaciones = ubicacionRepository.findAll();

        // Filtra las ubicaciones que ya tienen asignada la tarea
        List<Ubicacion> availableUbicaciones = allUbicaciones.stream()
                .filter(ubicacion -> !ubicacion.getTareas().stream()
                        .anyMatch(tarea -> tarea.getId().equals(tareaId)))
                .collect(Collectors.toList());

        return availableUbicaciones;
    }
    public List<Ubicacion> findAllBySociedadOrderedById(Sociedad sociedad) {
        return ubicacionRepository.findAllBySociedadOrderById(sociedad);
    }

}

