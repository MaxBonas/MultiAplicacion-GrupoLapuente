package Lapuente.TareasUbicaciones.repositories;

import Lapuente.TareasUbicaciones.DTOs.InformeDTO;
import Lapuente.TareasUbicaciones.DTOs.TareaCumplidaDTO;
import Lapuente.TareasUbicaciones.ENUMs.Turno;
import Lapuente.TareasUbicaciones.entities.Admin;
import Lapuente.TareasUbicaciones.entities.Informe;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InformeRepository extends JpaRepository<Informe, Long> {
    List<Informe> findByFechaBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Informe> findByUbicacion(Ubicacion ubicacion);
    List<Informe> findByTurno(Turno turno);
    List<Informe> findByFechaBetweenAndTurnoAndUbicacionId(LocalDateTime start, LocalDateTime end, Turno turno, Long ubicacionId);
    List<Informe> findAllByUbicacionAndFechaBetweenAndTurno(Ubicacion ubicacion, LocalDateTime fechaInicio, LocalDateTime fechaFin, Turno turno);

}

