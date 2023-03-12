package Lapuente.TareasUbicaciones.services;

import Lapuente.TareasUbicaciones.entities.TareasDiarias;
import Lapuente.TareasUbicaciones.entities.Worker;
import Lapuente.TareasUbicaciones.repositories.TareasDiariasRepository;
import Lapuente.TareasUbicaciones.services.interfaces.TareasDiariasServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TareasDiariasService implements TareasDiariasServiceInterface {

    @Autowired
    private TareasDiariasRepository tareasDiariasRepository;

    @Override
    public List<TareasDiarias> getAllTareasDiarias() {
        return tareasDiariasRepository.findAll();
    }

    @Override
    public TareasDiarias getTareasDiariasById(Long id) {
        return tareasDiariasRepository.findById(id).orElse(null);
    }

    @Override
    public void saveOrUpdateTareasDiarias(TareasDiarias tareasDiarias) {
        tareasDiariasRepository.save(tareasDiarias);
    }

    @Override
    public void deleteTareasDiarias(Long id) {
        tareasDiariasRepository.deleteById(id);
    }

    @Override
    public List<TareasDiarias> getTareasDiariasByWorker(Worker worker) {
        return tareasDiariasRepository.findByWorker(worker);
    }

    @Override
    public List<TareasDiarias> getTareasDiariasByUbicacion(String ubicacion) {
        return tareasDiariasRepository.findByUbicacion(ubicacion);
    }

    @Override
    public void saveOrUpdateTareasDiarias(Long id, Boolean cumplida) {
        TareasDiarias tareasDiarias = tareasDiariasRepository.findById(id).orElse(null);
        tareasDiarias.setCumplida(cumplida);
        tareasDiariasRepository.save(tareasDiarias);
    }

    @Override
    public List<TareasDiarias> getTareasDiariasByWorker(Optional<Worker> byName) {
        return null;
    }
}