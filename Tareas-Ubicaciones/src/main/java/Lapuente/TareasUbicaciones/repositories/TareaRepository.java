package Lapuente.TareasUbicaciones.repositories;

import Lapuente.TareasUbicaciones.entities.Tarea;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
        List<Tarea> findByUbicaciones(Ubicacion ubicacion);
}
