package Lapuente.TareasUbicaciones.services;

import Lapuente.TareasUbicaciones.entities.Tarea;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.entities.Worker;
import Lapuente.TareasUbicaciones.repositories.TareaRepository;
import Lapuente.TareasUbicaciones.services.interfaces.TareaServiceInterface;
import Lapuente.TareasUbicaciones.services.interfaces.UbicacionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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
    public Tarea saveTarea(Tarea tarea) {
        return tareaRepository.save(tarea);
    }

    @Override
    public void deleteTareaById(Long id) {
        tareaRepository.deleteById(id);
    }

    @Override
    public List<Tarea> getTareasByWorker(Worker worker) {
        return tareaRepository.findByWorker(worker);
    }

    @Override
    public List<Tarea> getTareasByUbicacion(Long id) {
        return tareaRepository.findByUbicacionId(id);
    }

    @Override
    public void confirmarTarea(Long tareaId, Long ubicacionId, boolean tareaCumplida) {

    }


}