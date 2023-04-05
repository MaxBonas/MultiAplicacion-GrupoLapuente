package Lapuente.TareasUbicaciones.services;

import Lapuente.TareasUbicaciones.DTOs.InformeDTO;
import Lapuente.TareasUbicaciones.DTOs.TareaCumplidaDTO;
import Lapuente.TareasUbicaciones.DTOs.UbicacionDTO;
import Lapuente.TareasUbicaciones.DTOs.WorkerDTO;
import Lapuente.TareasUbicaciones.ENUMs.Turno;
import Lapuente.TareasUbicaciones.entities.*;
import Lapuente.TareasUbicaciones.repositories.*;
import Lapuente.TareasUbicaciones.services.interfaces.WorkerServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkerService implements WorkerServiceInterface {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private TareaCumplidaRepository tareaCumplidaRepository;

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InformeRepository informeRepository;

    @Override
    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    @Override
    public Worker getWorkerById(Long id) {
        return workerRepository.findById(id).orElse(null);
    }

    @Override
    public Worker saveWorker(WorkerDTO workerDTO) {
        Worker worker = new Worker();
        worker.setId(workerDTO.getId());
        worker.setName(workerDTO.getName());
        worker.setPassword(workerDTO.getPassword());
        worker.setCargo(workerDTO.getCargo());

        return workerRepository.save(worker);
    }

    @Override
    public void deleteWorkerById(Long id) {
        workerRepository.deleteById(id);
    }

    @Override
    public Worker findByName(String name) {
        return workerRepository.findByName(name);
    }

    @Override
    public void informarTareasCumplidas(Long ubicacionId, Turno turno, List<Long> tareasCumplidasIds, UserDetails userDetails, String comentario) {
        Worker worker = workerRepository.findByName(userDetails.getUsername());
        Ubicacion ubicacion = ubicacionRepository.findById(ubicacionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Ubicacion not found"));
        List<Tarea> tareas = tareaRepository.findByUbicaciones(ubicacion);

        LocalDateTime fechaCumplimiento = LocalDateTime.now();

        // Crea un nuevo InformeDTO y guárdalo en la base de datos
        List<TareaCumplidaDTO> tareasCumplidasDTO = new ArrayList<>();
        for (Tarea tarea : tareas) {
            boolean cumplida = tareasCumplidasIds.contains(tarea.getId());
            TareaCumplidaDTO tareaCumplidaDTO = new TareaCumplidaDTO(tarea.getId(), tarea.getName(), worker.getId(), ubicacion.getId(), cumplida, worker.getName(), fechaCumplimiento, turno);
            tareasCumplidasDTO.add(tareaCumplidaDTO);
        }
        InformeDTO informeDTO = new InformeDTO(fechaCumplimiento, ubicacion.getId(), turno, worker.getId(), comentario, tareasCumplidasDTO);
        Informe informe = new Informe(informeDTO, worker, ubicacion);
        informe = informeRepository.save(informe);

        for (Tarea tarea : tareas) {
            boolean cumplida = tareasCumplidasIds.contains(tarea.getId());
            TareaCumplida tareaCumplida = new TareaCumplida(tarea, worker, ubicacion, cumplida, fechaCumplimiento, turno, informe);
            tareaCumplidaRepository.save(tareaCumplida);
        }
    }


    @Override
    public List<TareaCumplida> getTareasCumplidasByUbicacionYPeriodo(Long ubicacionId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return tareaCumplidaRepository.findByTareaUbicacionesIdAndFechaCumplimientoBetween(ubicacionId, fechaInicio, fechaFin);
    }
    @Override
    public void cambiarPassword(Long workerId, String oldPassword, String newPassword) {
        Worker worker = workerRepository.findById(workerId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe el trabajador con id: " + workerId + " en la base de datos"));
        if (passwordEncoder.matches(oldPassword, worker.getPassword())) {
            worker.setPassword(passwordEncoder.encode(newPassword));
            workerRepository.save(worker);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña actual proporcionada es incorrecta.");
        }
    }
    @Override
    public List<TareaCumplida> getTareasCumplidasByWorkerAndTurno(Long workerId, Turno turno) {
        return tareaCumplidaRepository.findByWorkerIdAndTurno(workerId, turno);
    }

    @Override
    public Worker updateWorker(Long id, WorkerDTO workerDTO) {
        Worker worker = new Worker(workerDTO.getName(), workerDTO.getPassword(), workerDTO.getCargo());
        worker.setId(id);
        return workerRepository.save(worker);
    }
}
