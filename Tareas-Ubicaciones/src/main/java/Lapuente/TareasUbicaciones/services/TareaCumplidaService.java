package Lapuente.TareasUbicaciones.services;

import Lapuente.TareasUbicaciones.DTOs.TareaCumplidaDTO;
import Lapuente.TareasUbicaciones.entities.*;
import Lapuente.TareasUbicaciones.repositories.*;
import Lapuente.TareasUbicaciones.services.interfaces.TareaCumplidaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TareaCumplidaService implements TareaCumplidaServiceInterface {

    @Autowired
    private TareaCumplidaRepository tareaCumplidaRepository;

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;
    @Autowired
    private InformeRepository informeRepository;

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
        return tareaCumplidaRepository.findByTarea_Ubicaciones(ubicacion);
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

    @Override
    public TareaCumplida save(TareaCumplidaDTO tareaCumplidaDTO, Long informeId) {
        Tarea tarea = tareaRepository.findById(tareaCumplidaDTO.getTareaId()).orElseThrow(() -> new NoSuchElementException("Tarea no encontrada"));
        Worker worker = workerRepository.findById(tareaCumplidaDTO.getWorkerId()).orElseThrow(() -> new NoSuchElementException("Trabajador no encontrado"));
        Ubicacion ubicacion = ubicacionRepository.findById(tareaCumplidaDTO.getUbicacionId()).orElseThrow(() -> new NoSuchElementException("Ubicación no encontrada")); // Obtener la ubicación
        Informe informe = informeRepository.findById(informeId).orElseThrow(() -> new NoSuchElementException("Informe no encontrado"));
        TareaCumplida tareaCumplida = new TareaCumplida(tarea, worker, ubicacion, tareaCumplidaDTO.getCumplida(), tareaCumplidaDTO.getFechaCumplimiento(), tareaCumplidaDTO.getTurno(), informe);
        tareaCumplida.setUbicacion(ubicacion); // Establecer la ubicación en la tarea cumplida
        return tareaCumplidaRepository.save(tareaCumplida);
    }
}
