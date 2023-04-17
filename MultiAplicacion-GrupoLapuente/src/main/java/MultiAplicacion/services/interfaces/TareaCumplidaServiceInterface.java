package MultiAplicacion.services.interfaces;

import MultiAplicacion.DTOs.TareaCumplidaDTO;
import MultiAplicacion.ENUMs.Turno;
import MultiAplicacion.entities.TareaCumplida;
import MultiAplicacion.entities.Ubicacion;
import MultiAplicacion.entities.Worker;

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

