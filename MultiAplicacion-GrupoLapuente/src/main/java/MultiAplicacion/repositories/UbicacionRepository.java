package MultiAplicacion.repositories;

import MultiAplicacion.entities.Sociedad;
import MultiAplicacion.entities.Ubicacion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
    List<Ubicacion> findByName(String name);
    @EntityGraph(attributePaths = {"tareas"})
    List<Ubicacion> findBySociedad(Sociedad sociedad);
    List<Ubicacion> findAllBySociedadOrderById(Sociedad sociedad);
    @Query("SELECT u FROM Ubicacion u WHERE u.sociedad = :sociedad ORDER BY CASE u.id WHEN 1 THEN 1 WHEN 7 THEN 2 WHEN 6 THEN 3 WHEN 2 THEN 4 WHEN 4 THEN 5 WHEN 5 THEN 6 WHEN 3 THEN 7 ELSE 8 END")
    List<Ubicacion> findBySociedadCustomOrder(Sociedad sociedad);

}
