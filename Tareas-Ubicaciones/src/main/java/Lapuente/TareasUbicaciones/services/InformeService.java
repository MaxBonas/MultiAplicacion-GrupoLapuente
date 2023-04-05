package Lapuente.TareasUbicaciones.services;

import Lapuente.TareasUbicaciones.DTOs.TareaCumplidaDTO;
import Lapuente.TareasUbicaciones.ENUMs.Turno;
import Lapuente.TareasUbicaciones.entities.*;
import Lapuente.TareasUbicaciones.repositories.*;
import Lapuente.TareasUbicaciones.services.interfaces.InformeServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InformeService implements InformeServiceInterface {
    @Autowired
    InformeRepository informeRepository;
    @Autowired
    TareaCumplidaRepository tareaCumplidaRepository;
    @Autowired
    TareaRepository tareaRepository;
    @Autowired
    WorkerRepository workerRepository;
    @Autowired
    UbicacionRepository ubicacionRepository;

    @Override
    public Informe save(Informe informe) {
        return informeRepository.save(informe);
    }

    @Override
    public List<Informe> findAll() {
        return informeRepository.findAll();
    }

    @Override
    public Informe findById(Long id) {
        return informeRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        informeRepository.deleteById(id);
    }

    @Override
    public List<Informe> findByFechaBetween(LocalDateTime start, LocalDateTime end) {
        return informeRepository.findByFechaBetween(start, end);
    }

    @Override
    public List<Informe> findByTurno(Turno turno) {
        return informeRepository.findByTurno(turno);
    }


    @Override
    public List<Informe> findByUbicacion(Ubicacion ubicacion) {
        return informeRepository.findByUbicacion(ubicacion);
    }

    @Override
    public List<Informe> findByWorker(Worker worker) {
        return informeRepository.findByWorker(worker);
    }

    @Override
    public void updateTareasCumplidasDeInforme(Long informeId, List<TareaCumplidaDTO> tareasCumplidas) {
        Informe informe = informeRepository.findById(informeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Informe not found"));

        // Creamos la lista de tareas cumplidas
        List<TareaCumplida> tareaCumplidaList = tareasCumplidas.stream()
                .map(tareaCumplidaDTO -> {
                    Tarea tarea = tareaRepository.findById(tareaCumplidaDTO.getTareaId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Tarea not found"));
                    Worker worker = workerRepository.findById(tareaCumplidaDTO.getWorkerId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Worker not found"));
                    Ubicacion ubicacion = ubicacionRepository.findById(tareaCumplidaDTO.getUbicacionId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Ubicacion not found"));
                    return new TareaCumplida(tarea, worker, ubicacion, tareaCumplidaDTO.getCumplida(), tareaCumplidaDTO.getFechaCumplimiento(), tareaCumplidaDTO.getTurno(), informe);
                })
                .collect(Collectors.toList());

        // Actualizamos las tareas cumplidas del informe
        informe.setTareasCumplidas(tareaCumplidaList);

        // Guardamos el informe actualizado
        informeRepository.save(informe);
    }

    @Override
    public List<Informe> findByFechaBetweenAndTurnoAndUbicacionId(LocalDateTime start, LocalDateTime end, Turno turno, Long ubicacionId) {
        return informeRepository.findByFechaBetweenAndTurnoAndUbicacionId(start, end, turno, ubicacionId);
    }
}