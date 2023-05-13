package MultiAplicacion.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import MultiAplicacion.entities.Mensaje;
import MultiAplicacion.entities.User;
import MultiAplicacion.repositories.MensajeRepository;
import MultiAplicacion.repositories.UserRepository;
import MultiAplicacion.services.interfaces.MensajeServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MensajeService implements MensajeServiceInterface {

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mensaje createMensaje(Mensaje mensaje) {
        mensaje.setFechaHora(LocalDateTime.now());
        mensaje.setLeido(false);
        return mensajeRepository.save(mensaje);
    }

    @Override
    public Optional<Mensaje> findMensajeById(Long id) {
        return mensajeRepository.findById(id);
    }

    @Override
    public List<Mensaje> findMensajesByReceptor(User receptor) {
        return mensajeRepository.findByReceptor(receptor);
    }

    @Override
    public List<Mensaje> findMensajesByEmisor(User emisor) {
        return mensajeRepository.findByEmisor(emisor);
    }

    @Override
    public List<Mensaje> findMensajesCirculares() {
        return mensajeRepository.findByCircular(true);
    }

    @Override
    public List<Mensaje> findMensajesNoLeidos(User receptor) {
        return mensajeRepository.findByReceptorAndLeido(receptor, false);
    }

    @Override
    public Mensaje setMensajeLeido(Mensaje mensaje, boolean leido) {
        mensaje.setLeido(leido);
        return mensajeRepository.save(mensaje);
    }

    @Override
    public void deleteMensaje(Long id) {
        mensajeRepository.deleteById(id);
    }
}

