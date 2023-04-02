package Lapuente.TareasUbicaciones.services.interfaces;

import Lapuente.TareasUbicaciones.entities.User;

import java.util.List;

public interface UserServiceInterface {
    User getUserById(Long id);
    String deleteUserById(Long id);
    List<User> getAllUsers();
}

