package Lapuente.TareasUbicaciones.services;

import Lapuente.TareasUbicaciones.entities.Tarea;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.entities.Worker;
import Lapuente.TareasUbicaciones.repositories.TareaRepository;
import Lapuente.TareasUbicaciones.repositories.UbicacionRepository;
import Lapuente.TareasUbicaciones.repositories.WorkerRepository;
import Lapuente.TareasUbicaciones.services.interfaces.WorkerServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class WorkerService implements WorkerServiceInterface{
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public Worker getWorkerById(Long id) {
        return workerRepository.findById(id).orElse(null);
    }

    public Worker saveWorker(Worker worker) {
        return workerRepository.save(worker);
    }

    public void deleteWorkerById(Long id) {
        workerRepository.deleteById(id);
    }

    public Worker findByName(String name) {
        return workerRepository.findByName(name);

    }

    @Override
    public void asignarTareas(Long ubicacionId, Long tareaId, UserDetails userDetails) {
        Worker user = workerRepository.findByName(userDetails.getUsername());
        Tarea tarea1 = tareaRepository.findById(tareaId).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND,
                "No existe la tarea con id: " + tareaId + " en la base de datos"));
        Ubicacion ubicacion1 = ubicacionRepository.findBy(ubicacionId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe la ubicacion con id: " + ubicacionId + " en la base de datos"));

        tarea1.setUbicaciones(ubicacion1);
        tarea1.setWorker(user);
        user.setTareas();
            tarea1.setUbicacionId(ubicacionId);
            tareaRepository.save(tarea1);
        }
        workerRepository.asignarTarea(ubicacionId, tareaId);
    }

}