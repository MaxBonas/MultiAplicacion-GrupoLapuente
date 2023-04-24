package MultiAplicacion.services;

import MultiAplicacion.DTOs.TareaCumplidaDTO;
import MultiAplicacion.ENUMs.Turno;
import MultiAplicacion.entities.*;
import MultiAplicacion.repositories.*;
import MultiAplicacion.services.interfaces.TareaCumplidaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public TareaCumplida updateTareaCumplida(Long tareaCumplidaId, TareaCumplida tareaCumplida) {
        TareaCumplida existingTareaCumplida = tareaCumplidaRepository.findById(tareaCumplidaId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe la tarea cumplida con id: " + tareaCumplidaId + " en la base de datos"));

        TareaCumplida updatedTareaCumplida = new TareaCumplida();
        updatedTareaCumplida.setId(existingTareaCumplida.getId());
        updatedTareaCumplida.setCumplida(tareaCumplida.isCumplida());
        updatedTareaCumplida.setFechaCumplimiento(tareaCumplida.getFechaCumplimiento());
        updatedTareaCumplida.setTurno(tareaCumplida.getTurno());
        updatedTareaCumplida.setWorker(existingTareaCumplida.getWorker());
        updatedTareaCumplida.setTarea(existingTareaCumplida.getTarea());
        updatedTareaCumplida.setUbicacion(existingTareaCumplida.getUbicacion());
        updatedTareaCumplida.setInforme(existingTareaCumplida.getInforme());
        updatedTareaCumplida.setComentario(tareaCumplida.getComentario());

        return tareaCumplidaRepository.save(updatedTareaCumplida);
    }

    @Override
    public TareaCumplida save(TareaCumplidaDTO tareaCumplidaDTO) {

        TareaCumplida tareaCumplida = new TareaCumplida();
        tareaCumplida.setCumplida(tareaCumplidaDTO.isCumplida());
        tareaCumplida.setFechaCumplimiento(LocalDateTime.now());
        tareaCumplida.setTurno(tareaCumplidaDTO.getTurno());
        tareaCumplida.setWorker(workerRepository.findById(tareaCumplidaDTO.getWorkerId()).orElseThrow(() -> new NoSuchElementException("Trabajador no encontrado")));
        tareaCumplida.setComentario(tareaCumplidaDTO.getComentario());
        tareaCumplida.setTarea(tareaRepository.findById(tareaCumplidaDTO.getTareaId()).orElseThrow(() -> new NoSuchElementException("Tarea no encontrada")));
        tareaCumplida.setUbicacion(ubicacionRepository.findById(tareaCumplidaDTO.getUbicacionId()).orElseThrow(() -> new NoSuchElementException("Ubicacion no encontrada")));
        tareaCumplida.setInforme(null);
        return tareaCumplidaRepository.save(tareaCumplida);
    }

    @Override
    public List<TareaCumplida> findTareasCumplidasByUbicacionAndFechaAndTurnoAndCumplida(Ubicacion ubicacion, LocalDateTime fecha, Turno turno, boolean cumplida) {
        // Ajustar la hora de fecha a medianoche para comparar solo la parte de la fecha
        LocalDateTime fechaInicio = fecha.toLocalDate().atStartOfDay();
        LocalDateTime fechaFin = fecha.toLocalDate().atTime(23, 59, 59);

        // Buscar todas las tareas cumplidas que coincidan con la ubicación, fecha y turno proporcionados
        List<TareaCumplida> tareasCumplidas = tareaCumplidaRepository.findByUbicacionAndFechaCumplimientoBetweenAndTurnoAndCumplida(ubicacion, fechaInicio, fechaFin, turno, cumplida);

        return tareasCumplidas;
    }


}
