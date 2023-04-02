package Lapuente.TareasUbicaciones.services;

import Lapuente.TareasUbicaciones.DTOs.TareaDTO;
import Lapuente.TareasUbicaciones.entities.Tarea;
import Lapuente.TareasUbicaciones.repositories.TareaRepository;
import Lapuente.TareasUbicaciones.repositories.UbicacionRepository;
import Lapuente.TareasUbicaciones.services.interfaces.TareaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TareaService implements TareaServiceInterface {

    @Autowired
    private TareaRepository tareaRepository;

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
        Tarea tarea = new Tarea(tareaDTO.getNombre(), tareaDTO.getDescripcion(), null);
        return tareaRepository.save(tarea);
    }

    @Override
    public Tarea updateTarea(TareaDTO tareaDTO) {
        Tarea tarea = new Tarea(tareaDTO.getNombre(), tareaDTO.getDescripcion(), null);
        tarea.setId(tareaDTO.getId());
        return tareaRepository.save(tarea);
    }

    @Override
    public void deleteTareaById(Long id) {
        tareaRepository.deleteById(id);
    }

    @Override
    public List<Tarea> getTareasByUbicacion(Long id) {
        return tareaRepository.findByUbicacionId(id);
    }
}

