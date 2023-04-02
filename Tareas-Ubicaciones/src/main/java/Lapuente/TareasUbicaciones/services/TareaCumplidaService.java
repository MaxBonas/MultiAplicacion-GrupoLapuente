package Lapuente.TareasUbicaciones.services;

import Lapuente.TareasUbicaciones.DTOs.TareaCumplidaDTO;
import Lapuente.TareasUbicaciones.entities.TareaCumplida;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.entities.Worker;
import Lapuente.TareasUbicaciones.repositories.TareaCumplidaRepository;
import Lapuente.TareasUbicaciones.services.interfaces.TareaCumplidaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TareaCumplidaService implements TareaCumplidaServiceInterface {

    @Autowired
    private TareaCumplidaRepository tareaCumplidaRepository;

    @Override
    public List<TareaCumplida> getAllTareasCumplidas() {
        return tareaCumplidaRepository.findAll();
    }

    @Override
    public TareaCumplida getTareaCumplidaById(Long id) {
        return tareaCumplidaRepository.findById(id).orElse(null);
    }

    @Override
    public void saveOrUpdateTareaCumplida(TareaCumplida tareaCumplida) {
        tareaCumplidaRepository.save(tareaCumplida);
    }

    @Override
    public void deleteTareaCumplida(Long id) {
        tareaCumplidaRepository.deleteById(id);
    }

    @Override
    public List<TareaCumplida> getTareaCumplidaByWorker(Worker worker) {
        return tareaCumplidaRepository.findByWorker(worker);
    }

    @Override
    public List<TareaCumplida> getTareaCumplidaByUbicacion(Ubicacion ubicacion) {
        return tareaCumplidaRepository.findByTarea_Ubicacion(ubicacion);
    }

    @Override
    public TareaCumplida updateTareaCumplida(Long tareaCumplidaId, TareaCumplidaDTO tareaCumplidaDTO) {
        TareaCumplida tareaCumplida = tareaCumplidaRepository.findById(tareaCumplidaId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe la tarea cumplida con id: " + tareaCumplidaId + " en la base de datos"));
        tareaCumplida.setCumplida(tareaCumplidaDTO.isCumplida());
        tareaCumplida.setFechaCumplimiento(tareaCumplidaDTO.getFechaCumplimiento());
        tareaCumplida.setTurno(tareaCumplidaDTO.getTurno());
        return tareaCumplidaRepository.save(tareaCumplida);
    }
}
