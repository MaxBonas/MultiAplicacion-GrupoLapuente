package Lapuente.TareasUbicaciones.services.interfaces;

import Lapuente.TareasUbicaciones.entities.TareasDiarias;
import Lapuente.TareasUbicaciones.entities.Worker;

import java.util.List;
import java.util.Optional;

public interface TareasDiariasServiceInterface {

    List<TareasDiarias> getAllTareasDiarias();

    TareasDiarias getTareasDiariasById(Long id);

    void saveOrUpdateTareasDiarias(TareasDiarias tareasDiarias);

    void deleteTareasDiarias(Long id);

    List<TareasDiarias> getTareasDiariasByWorker(Worker worker);

    List<TareasDiarias> getTareasDiariasByUbicacion(String ubicacion);

    void saveOrUpdateTareasDiarias(Long id, Boolean confirmada);

    List<TareasDiarias> getTareasDiariasByWorker(Optional<Worker> byName);
}
