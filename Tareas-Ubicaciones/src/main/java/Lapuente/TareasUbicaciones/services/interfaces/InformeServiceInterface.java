package Lapuente.TareasUbicaciones.services.interfaces;

import Lapuente.TareasUbicaciones.DTOs.TareaCumplidaDTO;
import Lapuente.TareasUbicaciones.ENUMs.Turno;
import Lapuente.TareasUbicaciones.entities.Informe;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.entities.Worker;

import java.time.LocalDateTime;
import java.util.List;

public interface InformeServiceInterface {
    Informe save(Informe informe);
    List<Informe> findAll();
    Informe findById(Long id);
    void deleteById(Long id);
    List<Informe> findByFechaBetween(LocalDateTime start, LocalDateTime end);
    List<Informe> findByTurno(Turno turno);
    List<Informe> findByUbicacion(Ubicacion ubicacion);
    void updateTareasCumplidasDeInforme(Long informeId, List<TareaCumplidaDTO> tareasCumplidas);
    List<Informe> findByFechaBetweenAndTurnoAndUbicacionId(LocalDateTime start, LocalDateTime end, Turno turno, Long ubicacionId);
}
