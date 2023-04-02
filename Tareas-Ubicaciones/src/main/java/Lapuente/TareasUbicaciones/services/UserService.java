package Lapuente.TareasUbicaciones.services;

import Lapuente.TareasUbicaciones.entities.User;
import Lapuente.TareasUbicaciones.repositories.UserRepository;
import Lapuente.TareasUbicaciones.services.interfaces.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService implements UserServiceInterface {

    @Autowired
    UserRepository userRepository;


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

    @Override
    public String deleteUserById(Long id) {
        User user1 = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "A User with the given id doesn't exist"));
        userRepository.delete(user1);
        return "The User with id " + id + " has been removed correctly.";
    }

}

