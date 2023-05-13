package MultiAplicacion.entities;

import MultiAplicacion.ENUMs.Turno;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@SQLDelete(sql = "UPDATE tareacumplida SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
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

    @Column(nullable = true)
    private boolean cumplida;


    private LocalDateTime fechaCumplimiento;

    private Turno turno;

    private String comentario;
    @Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "informe_id", nullable = true)
    private Informe informe;

    public TareaCumplida() {}

    public TareaCumplida(Tarea tarea, Worker worker, Ubicacion ubicacion, boolean cumplida, LocalDateTime fechaCumplimiento, Turno turno, String comentario, Informe informe) {
        this.tarea = tarea;
        this.worker = worker;
        this.ubicacion = ubicacion;
        this.cumplida = cumplida;
        this.fechaCumplimiento = fechaCumplimiento;
        this.turno = turno;
        this.comentario = comentario;
        this.informe = informe;
    }

    public TareaCumplida(Long id, Tarea tarea, Worker worker, Ubicacion ubicacion, boolean cumplida, LocalDateTime fechaCumplimiento, Turno turno, String comentario, Informe informe) {
        this.id = id;
        this.tarea = tarea;
        this.worker = worker;
        this.ubicacion = ubicacion;
        this.cumplida = cumplida;
        this.fechaCumplimiento = fechaCumplimiento;
        this.turno = turno;
        this.comentario = comentario;
        this.informe = informe;
    }
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

    public void setCumplida(Boolean cumplida) {
        this.cumplida = cumplida;
    }

    public Informe getInforme() {
        return informe;
    }

    public void setInforme(Informe informe) {
        this.informe = informe;
    }

    public boolean isCumplida() {
        return cumplida;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}