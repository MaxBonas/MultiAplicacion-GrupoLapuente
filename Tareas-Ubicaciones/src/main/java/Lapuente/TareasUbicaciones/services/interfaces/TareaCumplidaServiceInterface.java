package Lapuente.TareasUbicaciones.services.interfaces;

import Lapuente.TareasUbicaciones.DTOs.TareaCumplidaDTO;
import Lapuente.TareasUbicaciones.entities.TareaCumplida;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.entities.Worker;

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
}

