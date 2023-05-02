package MultiAplicacion.services.interfaces;

import MultiAplicacion.entities.Sociedad;

import java.util.List;
import java.util.Optional;

public interface SociedadServiceInterface {

    List<Sociedad> findAll();

    Optional<Sociedad> findById(Long id);

    Sociedad save(Sociedad sociedad);

    void deleteById(Long id);

    Sociedad sociedadSeleccionada(Long id);
    Long getSociedadIdByUserName(String name);
    boolean existsById(Long id);
}
