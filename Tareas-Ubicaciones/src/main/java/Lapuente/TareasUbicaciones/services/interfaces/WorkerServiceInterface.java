package Lapuente.TareasUbicaciones.services.interfaces;

import Lapuente.TareasUbicaciones.entities.Worker;

import java.util.List;
import java.util.Optional;

public interface WorkerServiceInterface {
    List<Worker> getAllWorkers();

    Worker getWorkerById(Long id);

    Worker saveWorker(Worker worker);

    void deleteWorkerById(Long id);

    Worker findByName(String name);

    void asignarTareas(Long ubicacionId, Long tareaId);
}