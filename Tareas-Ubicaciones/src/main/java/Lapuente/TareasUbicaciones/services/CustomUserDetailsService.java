package Lapuente.TareasUbicaciones.services;

import Lapuente.TareasUbicaciones.entities.User;
import Lapuente.TareasUbicaciones.repositories.UserRepository;
import Lapuente.TareasUbicaciones.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Name does not exist."));
        return new CustomUserDetails(user);
    }
}
