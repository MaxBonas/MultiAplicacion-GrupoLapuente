package MultiAplicacion.services;

import MultiAplicacion.entities.Sociedad;
import MultiAplicacion.repositories.AdminRepository;
import MultiAplicacion.repositories.SociedadRepository;
import MultiAplicacion.repositories.WorkerRepository;
import MultiAplicacion.services.interfaces.SociedadServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
@Service
public class SociedadService implements SociedadServiceInterface {

    @Autowired
    private SociedadRepository sociedadRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private WorkerRepository workerRepository;

    @Override
    public List<Sociedad> findAll() {
        return sociedadRepository.findAll();
    }

    @Override
    public Optional<Sociedad> findById(Long id) {
        return sociedadRepository.findById(id);
    }

    @Override
    public Sociedad save(Sociedad sociedad) {
        return sociedadRepository.save(sociedad);
    }

    @Override
    public void deleteById(Long id) {
        sociedadRepository.deleteById(id);
    }

    @Override
    public Sociedad sociedadSeleccionada(Long id) {
        return sociedadRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Sociedad no encontrada con ID: " + id));
    }

    @Override
    public Long getSociedadIdByUserName(String name) {
        Sociedad sociedad = sociedadRepository.findByUsersName(name);
        if (sociedad == null) {
            sociedad = sociedadRepository.findByUsersName(name);
        }
        if (sociedad == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la sociedad del usuario");
        }
        return sociedad.getId();
    }

    @Override
    public boolean existsById(Long id) {
        return sociedadRepository.existsById(id);
    }
}
