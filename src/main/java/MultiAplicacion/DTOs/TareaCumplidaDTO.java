package MultiAplicacion.DTOs;

import MultiAplicacion.ENUMs.Turno;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class TareaCumplidaDTO {

    private Long id;
    private Long tareaId;
    @NotNull(message = "El nombre no puede ser nulo")
    @NotEmpty(message = "El nombre no puede estar vacío")
    private String tareaName;
    private Long workerId;
    private Long ubicacionId;
    private Boolean cumplida;
    private String workerName;
    private LocalDateTime fechaCumplimiento;
    private Turno turno;
    private String comentario;

    public TareaCumplidaDTO() {
    }

    public TareaCumplidaDTO(Long id, Long tareaId, String tareaName, Long workerId, Long ubicacionId, Boolean cumplida, String workerName, LocalDateTime fechaCumplimiento, Turno turno, String comentario) {
        this.id = id;
        this.tareaId = tareaId;
        this.tareaName = tareaName;
        this.workerId = workerId;
        this.ubicacionId = ubicacionId;
        this.cumplida = cumplida;
        this.workerName = workerName;
        this.fechaCumplimiento = fechaCumplimiento;
        this.turno = turno;
        this.comentario = comentario;
    }

    public TareaCumplidaDTO(Long tareaId, String tareaName, Long workerId, Long ubicacionId, Boolean cumplida, String workerName, LocalDateTime fechaCumplimiento, Turno turno, String comentario) {
        this.tareaId = tareaId;
        this.tareaName = tareaName;
        this.workerId = workerId;
        this.ubicacionId = ubicacionId;
        this.cumplida = cumplida;
        this.workerName = workerName;
        this.fechaCumplimiento = fechaCumplimiento;
        this.turno = turno;
        this.comentario = comentario;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTareaId() {
        return tareaId;
    }

    public void setTareaId(Long tareaId) {
        this.tareaId = tareaId;
    }

    public String getTareaName() {
        return tareaName;
    }

    public void setTareaName(String tareaName) {
        this.tareaName = tareaName;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public LocalDateTime getFechaCumplimiento() {
        return fechaCumplimiento;
    }

    public void setFechaCumplimiento(LocalDateTime fechaCumplimiento) {
        this.fechaCumplimiento = fechaCumplimiento;
    }
    public boolean isCumplida() {
        return cumplida;
    }

    public void setCumplida(boolean cumplida) {
        this.cumplida = cumplida;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Long getUbicacionId() {
        return ubicacionId;
    }

    public void setUbicacionId(Long ubicacionId) {
        this.ubicacionId = ubicacionId;
    }

    public Boolean getCumplida() {
        return cumplida;
    }

    public void setCumplida(Boolean cumplida) {
        this.cumplida = cumplida;
    }

    public String getComentario() {
        return comentario;
    }
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
