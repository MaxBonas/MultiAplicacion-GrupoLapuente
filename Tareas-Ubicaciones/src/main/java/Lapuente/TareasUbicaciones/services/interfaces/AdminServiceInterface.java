package Lapuente.TareasUbicaciones.services.interfaces;

import Lapuente.TareasUbicaciones.entities.Admin;

import java.util.List;
import java.util.Optional;

public interface AdminServiceInterface {
    Admin createAdmin(String name, String email, String password);
    Optional<Admin> getAdminByName(String name);
    Optional<Admin> getAdminByEmail(String email);
}