package MultiAplicacion.repositories;

import MultiAplicacion.ENUMs.Turno;
import MultiAplicacion.entities.Informe;
import MultiAplicacion.entities.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
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

