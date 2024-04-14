package MultiAplicacion.entities;

import MultiAplicacion.ENUMs.Turno;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
// La anotación @SQLDelete se usa para modificar el comportamiento de la operación de eliminación. En este caso, en lugar de eliminar registros, se marca como eliminado.
@SQLDelete(sql = "UPDATE tareacumplida SET deleted = 1 WHERE id = ?")
// La anotación @Where se utiliza para filtrar los registros que tienen el campo "deleted" igual a 0 (no eliminados).
@Where(clause = "deleted = 0")
public class TareaCumplida {

    // La anotación @Id y @GeneratedValue se utilizan para auto-generar la clave primaria de la tabla.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // La anotación @ManyToOne indica que existe una relación de muchos a uno entre las entidades TareaCumplida, Tarea, Worker y Ubicacion.
    // FetchType.LAZY significa que la inicialización de la relación se retrasa hasta que se accede a la propiedad.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarea_id")
    private Tarea tarea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean cumplida;

    private LocalDateTime fechaCumplimiento;

    private Turno turno;

    private String comentario;

    @Column(name = "deleted", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private boolean deleted = false;

    // Constructor por defecto de la clase TareaCumplida
    public TareaCumplida() {}

    // Constructores de la clase TareaCumplida con parámetros
    // Estos constructores inicializan un objeto TareaCumplida con los atributos específicos
    public TareaCumplida(Tarea tarea, Worker worker, Ubicacion ubicacion, boolean cumplida, LocalDateTime fechaCumplimiento, Turno turno, String comentario) {
        this.tarea = tarea;
        this.worker = worker;
        this.ubicacion = ubicacion;
        this.cumplida = cumplida;
        this.fechaCumplimiento = fechaCumplimiento;
        this.turno = turno;
        this.comentario = comentario;
    }

    public TareaCumplida(Long id, Tarea tarea, Worker worker, Ubicacion ubicacion, boolean cumplida, LocalDateTime fechaCumplimiento, Turno turno, String comentario) {
        this.id = id;
        this.tarea = tarea;
        this.worker = worker;
        this.ubicacion = ubicacion;
        this.cumplida = cumplida;
        this.fechaCumplimiento = fechaCumplimiento;
        this.turno = turno;
        this.comentario = comentario;
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