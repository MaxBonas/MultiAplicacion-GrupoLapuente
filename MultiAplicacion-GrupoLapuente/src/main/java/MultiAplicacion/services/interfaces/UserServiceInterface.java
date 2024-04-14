package MultiAplicacion.services.interfaces;

import MultiAplicacion.entities.User;

import java.util.List;

public interface UserServiceInterface {
    User getUserById(Long id);
    void deleteUserById(Long id);
    List<User> getAllUsers();
    List<User> findAllBySociedadId(Long sociedadId);
}

