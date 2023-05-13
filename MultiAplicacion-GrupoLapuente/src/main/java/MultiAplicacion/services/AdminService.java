package MultiAplicacion.services;

import MultiAplicacion.entities.Admin;
import MultiAplicacion.entities.Role;
import MultiAplicacion.entities.Sociedad;
import MultiAplicacion.entities.Worker;
import MultiAplicacion.repositories.AdminRepository;
import MultiAplicacion.repositories.RoleRepository;
import MultiAplicacion.repositories.SociedadRepository;
import MultiAplicacion.services.interfaces.AdminServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AdminService implements AdminServiceInterface {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SociedadRepository sociedadRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;

    @Override
    public Admin createAdmin(String name, String email, String password, Long sociedadId) {
        Admin admin = new Admin(name, email, passwordEncoder.encode(password), sociedadRepository.findById(sociedadId).orElseThrow());;
        // Guarda el worker en la base de datos antes de crear y guardar el role
        Admin savedAdmin = adminRepository.save(admin);
        roleRepository.save(new Role("ADMIN", savedAdmin));

        return savedAdmin;
    }

    @Override
    public Optional<Admin> findAdminByName(String name) {
        return adminRepository.findByName(name);
    }

    @Override
    public Optional<Admin> findAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    @Override
    public void adminDeleteById(Long id) {
        userService.deleteUserById(id);
    }
}
