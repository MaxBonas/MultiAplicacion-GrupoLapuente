package MultiAplicacion.repositories;

import MultiAplicacion.DTOs.TareaCumplidaDTO;
import MultiAplicacion.ENUMs.Turno;
import MultiAplicacion.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TareaCumplidaRepository extends JpaRepository<TareaCumplida, Long> {
    List<TareaCumplida> findByWorker(Worker worker);
    List<TareaCumplida> findByTarea_Ubicaciones(Ubicacion ubicacion);
    List<TareaCumplida> findByWorkerIdAndTurno(Long workerId, Turno turno);
    List<TareaCumplida> findByTareaUbicacionesIdAndFechaCumplimientoBetween(Long ubicacionId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<TareaCumplida> findByUbicacionAndFechaCumplimientoAndTurno(Ubicacion ubicacion, LocalDateTime fecha, Turno turno);
    List<TareaCumplida> findByUbicacionAndFechaCumplimientoBetweenAndTurno(Ubicacion ubicacion, LocalDateTime fechaInicio, LocalDateTime fechaFin, Turno turno);
    Optional<TareaCumplida> findByUbicacionAndTareaAndFechaCumplimientoBetweenAndTurno(Ubicacion ubicacion, Tarea tarea, LocalDateTime fechaInicio, LocalDateTime fechaFin, Turno turno);
    List<TareaCumplida> findByUbicacionAndFechaCumplimientoAndTurnoAndCumplida(Ubicacion ubicacion, LocalDateTime fecha, Turno turno, boolean cumplida);
    List<TareaCumplida> findByUbicacionIdAndFechaCumplimientoBetween(Long ubicacionId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<TareaCumplida> findByUbicacionAndFechaCumplimientoBetweenAndTurnoAndCumplida(Ubicacion ubicacion, LocalDateTime fechaInicio, LocalDateTime fechaFin, Turno turno, boolean cumplida);
}