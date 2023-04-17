package MultiAplicacion.services;

import MultiAplicacion.DTOs.TareaCumplidaDTO;
import MultiAplicacion.ENUMs.Turno;
import MultiAplicacion.entities.*;
import MultiAplicacion.repositories.*;
import MultiAplicacion.services.interfaces.InformeServiceInterface;
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
    public Informe save(Informe informe) {
        LocalDateTime fechaInicio = informe.getFecha().toLocalDate().atStartOfDay();
        LocalDateTime fechaFin = informe.getFecha().toLocalDate().atTime(23, 59, 59);
        List<Informe> informesExistente = informeRepository.findAllByUbicacionAndFechaBetweenAndTurno(informe.getUbicacion(), fechaInicio, fechaFin, informe.getTurno());

        if (!informesExistente.isEmpty()) {
            Informe informeExistente = informesExistente.get(0);
            informeExistente.setComentario(informe.getComentario());
            return informeRepository.save(informeExistente);
        }

        return informeRepository.save(informe);
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