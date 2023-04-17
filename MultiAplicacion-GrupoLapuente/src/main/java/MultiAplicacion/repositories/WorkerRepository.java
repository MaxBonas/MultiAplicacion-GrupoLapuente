package MultiAplicacion.repositories;

import MultiAplicacion.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Optional<Worker> findByName(String name);
    List<Worker> findBySociedadId(Long sociedadId);
}