package Lapuente.TareasUbicaciones.services;

import Lapuente.TareasUbicaciones.entities.Admin;
import Lapuente.TareasUbicaciones.repositories.AdminRepository;
import Lapuente.TareasUbicaciones.services.interfaces.AdminServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService implements AdminServiceInterface {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin createAdmin(String name, String email, String password) {
        Admin admin = new Admin(name, email, password);
        return adminRepository.save(admin);
    }

    @Override
    public Optional<Admin> getAdminByName(String name) {
        return adminRepository.findByName(name);
    }

    @Override
    public Optional<Admin> getAdminByEmail(String email) {
        return adminRepository.findAdminByEmail(email);
    }
}