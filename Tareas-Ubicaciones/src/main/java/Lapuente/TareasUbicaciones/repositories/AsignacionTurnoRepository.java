package Lapuente.TareasUbicaciones.repositories;

import Lapuente.TareasUbicaciones.entities.AsignacionTurno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignacionTurnoRepository extends JpaRepository<AsignacionTurno, Long> {
}