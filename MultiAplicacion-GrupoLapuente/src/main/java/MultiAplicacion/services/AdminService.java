package MultiAplicacion.services;

import MultiAplicacion.entities.Admin;
import MultiAplicacion.entities.Role;
import MultiAplicacion.repositories.AdminRepository;
import MultiAplicacion.repositories.RoleRepository;
import MultiAplicacion.repositories.SociedadRepository;
import MultiAplicacion.services.interfaces.AdminServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
        if(name == null || email == null || password == null || sociedadId == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        Admin admin = new Admin(name, email, passwordEncoder.encode(password),
                sociedadRepository.findById(sociedadId)
                        .orElseThrow(() -> new EntityNotFoundException("Sociedad with id " + sociedadId + " not found"))
        );


        // Guarda el worker en la base de datos antes de crear y guardar el role
        Admin savedAdmin = adminRepository.save(admin);
        roleRepository.save(new Role("ADMIN", savedAdmin));

        return savedAdmin;
    }

    @Override
    public Optional<Admin> findAdminByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("El Nombre no puede ser nulo");
        }
        return adminRepository.findByName(name);
    }

    @Override
    public Optional<Admin> findAdminByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("El Email no puede ser nulo");
        }
        return adminRepository.findByEmail(email);
    }


    @Override
    public void adminDeleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("La ID no puede ser nula");
        }
        userService.deleteUserById(id);
    }

    public void changePasswordAdmin(UserDetails userDetails, String newPassword) {
        if (userDetails == null || userDetails.getUsername() == null) {
            throw new IllegalArgumentException("Los detalles del usuario no pueden ser nulos");
        }

        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("La nueva contraseña no puede ser nula o vacía");
        }

        Admin admin = adminRepository.findByName(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Admin no encontrado con nombre de usuario: " + userDetails.getUsername()));

        admin.setPassword(passwordEncoder.encode(newPassword));
        adminRepository.save(admin);
    }

}
