package MultiAplicacion.services;

import MultiAplicacion.entities.Role;
import MultiAplicacion.entities.User;
import MultiAplicacion.repositories.RoleRepository;
import MultiAplicacion.repositories.UserRepository;
import MultiAplicacion.services.interfaces.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class UserService implements UserServiceInterface {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private EntityManager entityManager;


    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "A User with the given id doesn't exist"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUserById(Long id) {
        deleteRoleByUserId(id);
        deleteUserOnlyById(id);
    }

    @Transactional
    public void deleteRoleByUserId(Long id) {
        try {
            Role role = roleRepository.findByUserId(id).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "No existe el role asociado al usuario con id: " + id + " en la base de datos"));
            role.setDeleted(true);
            roleRepository.save(role);
        } catch (ResponseStatusException e) {
            // Log or handle the exception as needed
        }
    }

    @Transactional
    public void deleteUserOnlyById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "A User with the given id doesn't exist"));
        // Refresh the user entity
        entityManager.refresh(user);
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public List<User> findAllBySociedadId(Long sociedadId) {
        return userRepository.findBySociedadId(sociedadId);
    }
}

