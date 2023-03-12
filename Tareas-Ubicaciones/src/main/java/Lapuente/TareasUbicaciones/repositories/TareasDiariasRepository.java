package Lapuente.TareasUbicaciones.repositories;

import Lapuente.TareasUbicaciones.entities.TareasDiarias;
import Lapuente.TareasUbicaciones.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareasDiariasRepository extends JpaRepository<TareasDiarias, Long> {

        List<TareasDiarias> findByWorker(Worker worker);

        List<TareasDiarias> findByUbicacion(String ubicacion);

}