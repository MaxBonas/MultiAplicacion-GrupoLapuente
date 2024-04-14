package MultiAplicacion.repositories;

import MultiAplicacion.entities.Tarea;
import MultiAplicacion.entities.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
        List<Tarea> findByUbicaciones(Ubicacion ubicacion);
        @Query("SELECT t FROM Tarea t WHERE t.id IN (SELECT MIN(t2.id) FROM Tarea t2 GROUP BY t2.name)")
        List<Tarea> findAllDistinctByName();
        List<Tarea> findAllByOrderById();
}
