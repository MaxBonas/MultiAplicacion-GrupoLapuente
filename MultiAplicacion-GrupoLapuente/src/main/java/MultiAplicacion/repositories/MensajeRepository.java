package MultiAplicacion.repositories;

import MultiAplicacion.entities.Mensaje;
import MultiAplicacion.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByReceptor(User receptor);
    List<Mensaje> findByEmisor(User emisor);
    List<Mensaje> findByCircular(boolean circular);
    List<Mensaje> findByReceptorAndLeido(User receptor, boolean leido);
}
