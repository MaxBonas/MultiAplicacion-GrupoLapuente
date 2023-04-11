package Lapuente.TareasUbicaciones.services.interfaces;

import Lapuente.TareasUbicaciones.DTOs.TareaCumplidaDTO;
import Lapuente.TareasUbicaciones.ENUMs.Turno;
import Lapuente.TareasUbicaciones.entities.TareaCumplida;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.entities.Worker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TareaCumplidaServiceInterface {
    List<TareaCumplida> getAllTareasCumplidas();
    TareaCumplida getTareaCumplidaById(Long id);
    void deleteTareaCumplida(Long id);
    List<TareaCumplida> getTareaCumplidaByWorker(Worker worker);
    List<TareaCumplida> getTareaCumplidaByUbicacion(Ubicacion ubicacion);
    TareaCumplida updateTareaCumplida(Long tareaCumplidaId, TareaCumplidaDTO tareaCumplidaDTO);
    void saveOrUpdateTareaCumplida(TareaCumplida tareaCumplida);
    TareaCumplida save(TareaCumplidaDTO tareaCumplidaDTO, Long informeId);
    List<TareaCumplida> findTareasCumplidasByUbicacionAndFechaAndTurno(Ubicacion ubicacion, LocalDateTime fecha, Turno turno);
    Optional<String> findComentarioByUbicacionAndFechaAndTurno(Ubicacion ubicacion, LocalDateTime fecha, Turno turno);
    List<TareaCumplida> findTareasNoInformadasByUbicacionAndFechaAndTurno(Ubicacion ubicacion, LocalDateTime fecha, Turno turno);

}

