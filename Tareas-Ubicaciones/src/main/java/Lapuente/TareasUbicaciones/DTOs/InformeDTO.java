package Lapuente.TareasUbicaciones.DTOs;

import Lapuente.TareasUbicaciones.ENUMs.Turno;

import java.time.LocalDateTime;
import java.util.List;

public class InformeDTO {
    private Long id;
    private LocalDateTime fecha;
    private UbicacionDTO ubicacion;
    private Turno turno;
    private List<TareaCumplidaDTO> tareasCumplidas;
    private Long workerId; // Añadido
    private Long parejaId; // Añadido

    // Constructor, getters y setters


    public InformeDTO(LocalDateTime fecha, UbicacionDTO ubicacion, Turno turno, List<TareaCumplidaDTO> tareasCumplidas, Long workerId, Long parejaId) {
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.turno = turno;
        this.tareasCumplidas = tareasCumplidas;
        this.workerId = workerId;
        this.parejaId = parejaId;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public Long getParejaId() {
        return parejaId;
    }

    public void setParejaId(Long parejaId) {
        this.parejaId = parejaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public UbicacionDTO getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(UbicacionDTO ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public List<TareaCumplidaDTO> getTareasCumplidas() {
        return tareasCumplidas;
    }

    public void setTareasCumplidas(List<TareaCumplidaDTO> tareasCumplidas) {
        this.tareasCumplidas = tareasCumplidas;
    }
}
