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
    @Query("SELECT u FROM Ubicacion u WHERE u.sociedad = :sociedad ORDER BY FIELD(u.id, 1, 7, 6, 2, 4, 5, 3)")
    List<Ubicacion> findBySociedadCustomOrder(Sociedad sociedad);

}
