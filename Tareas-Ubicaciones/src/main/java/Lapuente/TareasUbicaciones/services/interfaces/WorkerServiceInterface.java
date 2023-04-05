package Lapuente.TareasUbicaciones.services.interfaces;

import Lapuente.TareasUbicaciones.DTOs.WorkerDTO;
import Lapuente.TareasUbicaciones.ENUMs.Turno;
import Lapuente.TareasUbicaciones.entities.TareaCumplida;
import Lapuente.TareasUbicaciones.entities.Worker;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkerServiceInterface {
    List<Worker> getAllWorkers();
    Worker getWorkerById(Long id);
    Worker saveWorker(WorkerDTO workerDTO);
    void deleteWorkerById(Long id);
    Worker findByName(String name);
    void informarTareasCumplidas(Long ubicacionId, Turno turno, List<Long> tareasCumplidasIds, UserDetails userDetails, String comentario);
    List<TareaCumplida> getTareasCumplidasByUbicacionYPeriodo(Long ubicacionId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    void cambiarPassword(Long workerId, String oldPassword, String newPassword);
    List<TareaCumplida> getTareasCumplidasByWorkerAndTurno(Long workerId, Turno turno);
    Worker updateWorker(Long id, WorkerDTO workerDTO);
}
