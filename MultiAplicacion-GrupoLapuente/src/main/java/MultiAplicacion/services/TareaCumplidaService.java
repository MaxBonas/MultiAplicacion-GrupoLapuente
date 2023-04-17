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
        LocalDateTime fechaInicio = tareaCumplidaDTO.getFechaCumplimiento().toLocalDate().atStartOfDay();
        LocalDateTime fechaFin = tareaCumplidaDTO.getFechaCumplimiento().toLocalDate().atTime(23, 59, 59);

        List<Informe> informes = informeRepository.findAllByUbicacionAndFechaBetweenAndTurno(ubicacion, fechaInicio, fechaFin, tareaCumplidaDTO.getTurno());

        for (Informe informeExistente : informes) {
            for (TareaCumplida tareaCumplidaExistente : informeExistente.getTareasCumplidas()) {
                if (tareaCumplidaExistente.getTarea().equals(tarea) && tareaCumplidaExistente.getInforme().equals(informe)) {
                    throw new IllegalArgumentException("Tarea ya reportada.");
                }
            }
        }

        // Si no existe, crear y guardar una nueva tarea cumplida
        TareaCumplida tareaCumplida = new TareaCumplida(tarea, worker, ubicacion, tareaCumplidaDTO.getCumplida(), tareaCumplidaDTO.getFechaCumplimiento(), tareaCumplidaDTO.getTurno(), informe);
        tareaCumplida.setUbicacion(ubicacion);
        return tareaCumplidaRepository.save(tareaCumplida);
    }

    @Override
    public List<TareaCumplida> findTareasCumplidasByUbicacionAndFechaAndTurno(Ubicacion ubicacion, LocalDateTime fecha, Turno turno) {
        // Ajustar la hora de fecha a medianoche para comparar solo la parte de la fecha
        LocalDateTime fechaInicio = fecha.toLocalDate().atStartOfDay();
        LocalDateTime fechaFin = fecha.toLocalDate().atTime(23, 59, 59);

        // Buscar todos los informes que coincidan con la ubicación, fecha y turno proporcionados
        List<Informe> informes = informeRepository.findAllByUbicacionAndFechaBetweenAndTurno(ubicacion, fechaInicio, fechaFin, turno);

        // Crear una lista para recolectar las tareas cumplidas
        List<TareaCumplida> tareasCumplidas = new ArrayList<>();

        // Iterar sobre los informes y recolectar las tareas cumplidas que coincidan con los criterios
        for (Informe informe : informes) {
            for (TareaCumplida tareaCumplida : informe.getTareasCumplidas()) {
                if (tareaCumplida.getUbicacion().equals(ubicacion) && tareaCumplida.getFechaCumplimiento().isAfter(fechaInicio) && tareaCumplida.getFechaCumplimiento().isBefore(fechaFin) && tareaCumplida.getTurno().equals(turno)) {
                    tareasCumplidas.add(tareaCumplida);
                }
            }
        }

        return tareasCumplidas;
    }

    @Override
    public Optional<String> findComentarioByUbicacionAndFechaAndTurno(Ubicacion ubicacion, LocalDateTime fecha, Turno turno) {
        // Asegúrate de tener el objeto de acceso a datos (Repository) de Informe
        // inyectado en el servicio (por ejemplo, informeRepository)

        // Ajustar la hora de fecha a medianoche para comparar solo la parte de la fecha
        LocalDateTime fechaInicio = fecha.toLocalDate().atStartOfDay();
        LocalDateTime fechaFin = fecha.toLocalDate().atTime(23, 59, 59);

        // Buscar todos los informes que coincidan con la ubicación, fecha y turno proporcionados
        List<Informe> informes = informeRepository.findAllByUbicacionAndFechaBetweenAndTurno(ubicacion, fechaInicio, fechaFin, turno);

        // Si se encuentra un informe con el comentario, devuelve el comentario
        for (Informe informe : informes) {
            if (informe.getComentario() != null && !informe.getComentario().isEmpty()) {
                return Optional.of(informe.getComentario());
            }
        }

        // Si no se encuentra ningún comentario, devuelve un Optional vacío
        return Optional.empty();
    }
    @Override
    public List<TareaCumplida> findTareasNoInformadasByUbicacionAndFechaAndTurno(Ubicacion ubicacion, LocalDateTime fecha, Turno turno) {
        // Utiliza el método existente para encontrar todas las tareas cumplidas
        List<TareaCumplida> tareasCumplidas = findTareasCumplidasByUbicacionAndFechaAndTurno(ubicacion, fecha, turno);

        // Filtra las tareas cumplidas para quedarse solo con las que no han sido informadas
        List<TareaCumplida> tareasNoInformadas = tareasCumplidas.stream()
                .filter(tc -> tc.getInforme() == null)
                .collect(Collectors.toList());

        return tareasNoInformadas;
    }

}
