package MultiAplicacion.services.interfaces;

import MultiAplicacion.entities.Mensaje;
import MultiAplicacion.entities.User;

import java.util.List;
import java.util.Optional;

public interface MensajeServiceInterface {
    Mensaje createMensaje(Mensaje mensaje);
    Optional<Mensaje> findMensajeById(Long id);
    List<Mensaje> findMensajesByReceptor(User receptor);
    List<Mensaje> findMensajesByEmisor(User emisor);
    List<Mensaje> findMensajesCirculares();
    List<Mensaje> findMensajesNoLeidos(User receptor);
    Mensaje setMensajeLeido(Mensaje mensaje, boolean leido);
    void deleteMensaje(Long id);
}
