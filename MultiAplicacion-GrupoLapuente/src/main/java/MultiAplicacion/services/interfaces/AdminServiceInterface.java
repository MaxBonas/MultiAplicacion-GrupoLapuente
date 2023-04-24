package MultiAplicacion.services.interfaces;

import MultiAplicacion.entities.Admin;
import MultiAplicacion.entities.Sociedad;

import java.util.Optional;

public interface AdminServiceInterface {
    Admin createAdmin(String name, String email, String password, Long sociedadId);
    Optional<Admin> findAdminByName(String name);
    Optional<Admin> findAdminByEmail(String email);
}
