package MultiAplicacion.services.interfaces;

import MultiAplicacion.DTOs.TareaCumplidaDTO;
import MultiAplicacion.DTOs.WorkerDTO;
import MultiAplicacion.ENUMs.Turno;
import MultiAplicacion.entities.Informe;
import MultiAplicacion.entities.TareaCumplida;
import MultiAplicacion.entities.Ubicacion;
import MultiAplicacion.entities.Worker;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkerServiceInterface {
    List<Worker> getAllWorkers();
    Worker getWorkerById(Long id);
    Worker saveWorker(WorkerDTO workerDTO);
    void deleteWorkerById(Long id);
    Worker findByName(String name);
    void cambiarPassword(Long workerId, String oldPassword, String newPassword);
    Worker updateWorker(Long id, WorkerDTO workerDTO);
    List<Worker> getWorkersBySociedad(Long sociedadId);
    void crearTareasCumplidasVacias(Long workerId, Long ubicacionId, LocalDateTime fecha, Turno turno, String workers);
    void deleteRoleByUserId(Long userId);
}
