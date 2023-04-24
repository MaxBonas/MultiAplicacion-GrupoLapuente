package MultiAplicacion.repositories;

import MultiAplicacion.entities.Sociedad;
import MultiAplicacion.entities.Ubicacion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
    List<Ubicacion> findByName(String name);
    @EntityGraph(attributePaths = {"tareas"})
    List<Ubicacion> findBySociedad(Sociedad sociedad);
    List<Ubicacion> findAllBySociedadOrderById(Sociedad sociedad);

}
