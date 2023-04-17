package MultiAplicacion.services.interfaces;

import MultiAplicacion.entities.User;

import java.util.List;

public interface UserServiceInterface {
    User getUserById(Long id);
    String deleteUserById(Long id);
    List<User> getAllUsers();
}

