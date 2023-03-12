package Lapuente.TareasUbicaciones.services.interfaces;

import Lapuente.TareasUbicaciones.entities.Ubicacion;

import java.util.List;

public interface UbicacionServiceInterface {
    List<Ubicacion> findAll();
    Ubicacion findById(Long id);
    Ubicacion save(Ubicacion ubicacion);
    void deleteById(Long id);


}