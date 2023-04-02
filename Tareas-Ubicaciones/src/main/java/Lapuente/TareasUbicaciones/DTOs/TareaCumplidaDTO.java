package Lapuente.TareasUbicaciones.DTOs;

import Lapuente.TareasUbicaciones.ENUMs.Turno;
import Lapuente.TareasUbicaciones.entities.Worker;

import javax.persistence.Column;
import java.time.LocalDateTime;

public class TareaCumplidaDTO {

    private Long id;
    private Long tareaId;
    private String tareaNombre;
    private Long workerId;
    private Boolean cumplida;
    private String workerName;
    private LocalDateTime fechaCumplimiento;
    private Turno turno;
    @Column(length = 2000)
    private String comentario;


    public TareaCumplidaDTO() {
    }

    public TareaCumplidaDTO(Long tareaId, String tareaNombre, Long workerId, boolean cumplida, String workerName, LocalDateTime fechaCumplimiento, Turno turno, String comentario) {
        this.tareaId = tareaId;
        this.tareaNombre = tareaNombre;
        this.workerId = workerId;
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

    public String getTareaNombre() {
        return tareaNombre;
    }

    public void setTareaNombre(String tareaNombre) {
        this.tareaNombre = tareaNombre;
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

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
