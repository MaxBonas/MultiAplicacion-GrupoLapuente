package MultiAplicacion.services;

import MultiAplicacion.entities.Admin;
import MultiAplicacion.entities.Sociedad;
import MultiAplicacion.repositories.AdminRepository;
import MultiAplicacion.services.interfaces.AdminServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService implements AdminServiceInterface {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin createAdmin(String name, String email, String password, Sociedad sociedad) {
        Admin admin = new Admin(name, email, password, sociedad);
        return adminRepository.save(admin);
    }

    @Override
    public Optional<Admin> findAdminByName(String name) {
        return adminRepository.findByName(name);
    }

    @Override
    public Optional<Admin> findAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
}
