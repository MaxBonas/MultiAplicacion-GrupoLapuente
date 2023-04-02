package Lapuente.TareasUbicaciones.services;

import Lapuente.TareasUbicaciones.DTOs.TareaDTO;
import Lapuente.TareasUbicaciones.DTOs.UbicacionDTO;
import Lapuente.TareasUbicaciones.entities.Tarea;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.repositories.TareaRepository;
import Lapuente.TareasUbicaciones.repositories.UbicacionRepository;
import Lapuente.TareasUbicaciones.services.interfaces.UbicacionServiceInterface;
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

        // Guardar el objeto Ubicacion en el repositorio
        return ubicacionRepository.save(ubicacion);
    }



    @Override
    public List<Ubicacion> findByName(String name) {
        return ubicacionRepository.findByName(name);
    }

    @Override
    public void deleteById(Long id) {
        ubicacionRepository.deleteById(id);
    }

    // Método para obtener las tareas asociadas a una ubicación
    public List<Tarea> getTareasByUbicacionId(Long id) {
        return tareaRepository.findByUbicacionId(id);
    }

    @Override
    public Ubicacion addTareaAUbicacion(Long ubicacionId, TareaDTO tareaDTO) {
        Ubicacion ubicacion = findById(ubicacionId);
        Tarea tarea = new Tarea(tareaDTO.getNombre(), tareaDTO.getDescripcion(), ubicacion);
        tareaRepository.save(tarea);
        ubicacion.getTareas().add(tarea);
        return ubicacionRepository.save(ubicacion);
    }


    @Override
    public Ubicacion updateTareasDeUbicacion(Long ubicacionId, Set<TareaDTO> tareasDTO) {
        Ubicacion ubicacion = findById(ubicacionId);
        Set<Tarea> tareas = tareasDTO.stream()
                .map(tareaDTO -> new Tarea(tareaDTO.getNombre(), tareaDTO.getDescripcion(), ubicacion))
                .collect(Collectors.toSet());
        ubicacion.setTareas(tareas);
        tareaRepository.saveAll(tareas);
        return ubicacionRepository.save(ubicacion);
    }
}

