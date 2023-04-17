package MultiAplicacion.DTOs;

import MultiAplicacion.ENUMs.Turno;

import java.time.LocalDateTime;
import java.util.List;

public class InformeDTO {
    private Long id;
    private LocalDateTime fecha;
    private Long ubicacionId;
    private Turno turno;
    private List<TareaCumplidaDTO> tareasCumplidas;
    private String comentario;

    // Constructor, getters y setters

    public InformeDTO(LocalDateTime fecha, Long ubicacionId, Turno turno, String comentario, List<TareaCumplidaDTO> tareasCumplidas) {
        this.fecha = fecha;
        this.ubicacionId = ubicacionId;
        this.turno = turno;
        this.comentario = comentario;
        this.tareasCumplidas = tareasCumplidas;
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

    public Long getUbicacionId() {
        return ubicacionId;
    }

    public void setUbicacionId(Long ubicacionId) {
        this.ubicacionId = ubicacionId;
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

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
