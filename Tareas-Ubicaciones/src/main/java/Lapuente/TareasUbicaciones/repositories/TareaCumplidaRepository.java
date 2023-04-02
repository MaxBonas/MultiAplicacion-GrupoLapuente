package Lapuente.TareasUbicaciones.repositories;

import Lapuente.TareasUbicaciones.DTOs.TareaCumplidaDTO;
import Lapuente.TareasUbicaciones.ENUMs.Turno;
import Lapuente.TareasUbicaciones.entities.TareaCumplida;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface TareaCumplidaRepository extends JpaRepository<TareaCumplida, Long> {
    List<TareaCumplida> findByWorker(Worker worker);
    List<TareaCumplida> findByTarea_Ubicacion(Ubicacion ubicacion);
    List<TareaCumplida> findByWorkerIdAndTurno(Long workerId, Turno turno);
    List<TareaCumplida> findByTareaUbicacionIdAndFechaCumplimientoBetween(Long ubicacionId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    @Query("SELECT new Lapuente.TareasUbicaciones.DTOs.TareaCumplidaDTO(tc.tarea.id, tc.tarea.nombre, tc.worker.id, tc.cumplida, tc.worker.name, tc.fechaCumplimiento, tc.turno, tc.comentario) FROM TareaCumplida tc")
    List<TareaCumplidaDTO> findAllTareaCumplida();
}