package MultiAplicacion.services.interfaces;

import MultiAplicacion.DTOs.TareaCumplidaDTO;
import MultiAplicacion.ENUMs.Turno;
import MultiAplicacion.entities.Tarea;
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
    TareaCumplida updateTareaCumplida(Long tareaCumplidaId, TareaCumplida tareaCumplida);
    TareaCumplida save(TareaCumplidaDTO tareaCumplidaDTO);
    List<TareaCumplida> findTareasCumplidasByUbicacionAndFechaAndTurnoAndCumplida(Ubicacion ubicacion, LocalDateTime fecha, Turno turno, boolean cumplida);
}


