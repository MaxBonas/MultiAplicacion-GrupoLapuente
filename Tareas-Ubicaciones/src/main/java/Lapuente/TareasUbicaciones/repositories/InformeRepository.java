package Lapuente.TareasUbicaciones.repositories;

import Lapuente.TareasUbicaciones.ENUMs.Turno;
import Lapuente.TareasUbicaciones.entities.Admin;
import Lapuente.TareasUbicaciones.entities.Informe;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InformeRepository extends JpaRepository<Informe, Long> {
    List<Informe> findByFechaBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Informe> findByUbicacion(Ubicacion ubicacion);
    List<Informe> findByWorker(Worker worker);
    List<Informe> findByTurno(Turno turno);
}