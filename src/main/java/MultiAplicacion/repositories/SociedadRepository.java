package MultiAplicacion.repositories;

import MultiAplicacion.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SociedadRepository extends JpaRepository<Sociedad, Long> {
    Sociedad findByName(String name);

    Sociedad findByUsersName(String name);

//    Sociedad findbyUbicacion(Ubicacion ubicacion);
//    Sociedad findByUbicacionId(Long ubicacionId);
//    Sociedad findByUbicacionName(String ubicacionName);
//    Sociedad findByWorker(Worker worker);
//    Sociedad findByWorkerId(Long workerId);
//    Sociedad findByWorkerName(String workerName);
//    Sociedad findByAdmin(Admin admin);
//    Sociedad findByAdminId(Long adminId);
//    Sociedad findByAdminName(String adminName);
}
