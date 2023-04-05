package Lapuente.TareasUbicaciones.repositories;
import Lapuente.TareasUbicaciones.entities.Admin;
import Lapuente.TareasUbicaciones.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AdminRepository  extends JpaRepository<Admin, Long> {
        Optional<Admin> findByName(String name);
        Optional<Admin> findByEmail(String email);
}