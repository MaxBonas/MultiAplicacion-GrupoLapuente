package MultiAplicacion.repositories;

import MultiAplicacion.DTOs.TareaCumplidaDTO;
import MultiAplicacion.ENUMs.Turno;
import MultiAplicacion.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TareaCumplidaRepository extends JpaRepository<TareaCumplida, Long> {
    List<TareaCumplida> findByWorker(Worker worker);
    List<TareaCumplida> findByTarea_Ubicaciones(Ubicacion ubicacion);
    List<TareaCumplida> findByWorkerIdAndTurno(Long workerId, Turno turno);
    List<TareaCumplida> findByTareaUbicacionesIdAndFechaCumplimientoBetween(Long ubicacionId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    @Query("SELECT new MultiAplicacion.DTOs.TareaCumplidaDTO(tc.tarea.id, tc.tarea.name, tc.worker.id, tc.ubicacion.id, tc.cumplida, tc.worker.name, tc.fechaCumplimiento, tc.turno) FROM TareaCumplida tc")
    List<TareaCumplidaDTO> findAllTareaCumplida();
    Turno findTurnoByWorkerAndUbicacion(Worker worker, Ubicacion ubicacion);
    List<TareaCumplida> findByUbicacionAndFechaCumplimientoAndTurno(Ubicacion ubicacion, LocalDateTime fecha, Turno turno);
}