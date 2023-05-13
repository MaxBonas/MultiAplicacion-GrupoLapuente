package MultiAplicacion.repositories;

import MultiAplicacion.entities.Admin;
import MultiAplicacion.entities.Role;
import MultiAplicacion.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    void deleteByUserId(Long userId);
    Optional<Role> findByUserId(Long userId);
}
