package Lapuente.TareasUbicaciones.services;

import Lapuente.TareasUbicaciones.entities.TareasDiarias;
import Lapuente.TareasUbicaciones.entities.Worker;
import Lapuente.TareasUbicaciones.repositories.TareaCumplidaRepository;
import Lapuente.TareasUbicaciones.services.interfaces.TareasDiariasServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TareasDiariasService implements TareasDiariasServiceInterface {

    @Autowired
    private TareaCumplidaRepository tareaCumplidaRepository;

    @Override
    public List<TareasDiarias> getAllTareasDiarias() {
        return tareaCumplidaRepository.findAll();
    }

    @Override
    public TareasDiarias getTareasDiariasById(Long id) {
        return tareaCumplidaRepository.findById(id).orElse(null);
    }

    @Override
    public void saveOrUpdateTareasDiarias(TareasDiarias tareasDiarias) {
        tareaCumplidaRepository.save(tareasDiarias);
    }

    @Override
    public void deleteTareasDiarias(Long id) {
        tareaCumplidaRepository.deleteById(id);
    }

    @Override
    public List<TareasDiarias> getTareasDiariasByWorker(Worker worker) {
        return tareaCumplidaRepository.findByWorker(worker);
    }

    @Override
    public List<TareasDiarias> getTareasDiariasByUbicacion(String ubicacion) {
        return tareaCumplidaRepository.findByUbicacion(ubicacion);
    }

    @Override
    public void saveOrUpdateTareasDiarias(Long id, Boolean cumplida) {
        TareasDiarias tareasDiarias = tareaCumplidaRepository.findById(id).orElse(null);
        tareasDiarias.setCumplida(cumplida);
        tareaCumplidaRepository.save(tareasDiarias);
    }

    @Override
    public List<TareasDiarias> getTareasDiariasByWorker(Optional<Worker> byName) {
        return null;
    }
}