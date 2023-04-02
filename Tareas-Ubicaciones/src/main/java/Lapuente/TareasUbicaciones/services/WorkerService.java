package Lapuente.TareasUbicaciones.services;

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
import java.util.List;

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
        worker.setRoles(workerDTO.getRoles());

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
        List<Tarea> tareas = tareaRepository.findByUbicacionId(ubicacionId);

        LocalDateTime fechaCumplimiento = LocalDateTime.now();

        for (Tarea tarea : tareas) {
            boolean cumplida = tareasCumplidasIds.contains(tarea.getId());
            TareaCumplida tareaCumplida = new TareaCumplida(tarea, worker, cumplida, fechaCumplimiento, turno, comentario);
            tareaCumplidaRepository.save(tareaCumplida);
        }
    }


    @Override
    public List<TareaCumplida> getTareasCumplidasByUbicacionYPeriodo(Long ubicacionId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return tareaCumplidaRepository.findByTareaUbicacionIdAndFechaCumplimientoBetween(ubicacionId, fechaInicio, fechaFin);
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

}
