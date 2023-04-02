package Lapuente.TareasUbicaciones.entities;

import Lapuente.TareasUbicaciones.ENUMs.Turno;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TareaCumplida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarea_id")
    private Tarea tarea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @Column(nullable = true, columnDefinition = "TINYINT(1) DEFAULT NULL")
    private Boolean cumplida;


    private LocalDateTime fechaCumplimiento;

    private Turno turno;

    @Column(length = 2000) // Limitar el tamaño del comentario a 2000 caracteres, ajusta según tus necesidades
    private String comentario; // Añadir el atributo comentario

    public TareaCumplida(Tarea tarea, Worker worker, boolean cumplida, LocalDateTime fechaCumplimiento, Turno turno, String comentario) {
        this.tarea = tarea;
        this.worker = worker;
        this.cumplida = cumplida;
        this.fechaCumplimiento = fechaCumplimiento;
        this.turno = turno;
        this.comentario = comentario; // Incluir el comentario en el constructor
    }

    public TareaCumplida() {}


    // SETTERS Y GETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public boolean getCumplida() {
        return cumplida;
    }

    public void setCumplida(boolean cumplida) {
        this.cumplida = cumplida;
    }

    public LocalDateTime getFechaCumplimiento() {
        return fechaCumplimiento;
    }

    public void setFechaCumplimiento(LocalDateTime fechaCumplimiento) {
        this.fechaCumplimiento = fechaCumplimiento;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}