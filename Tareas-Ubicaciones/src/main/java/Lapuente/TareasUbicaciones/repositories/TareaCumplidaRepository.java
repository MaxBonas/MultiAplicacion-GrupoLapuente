package Lapuente.TareasUbicaciones.repositories;

import Lapuente.TareasUbicaciones.entities.TareaCumplida;
import Lapuente.TareasUbicaciones.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaCumplidaRepository extends JpaRepository<TareaCumplida, Long> {
}