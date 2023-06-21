package MultiAplicacion.entities;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ubicacion_tarea")
@SQLDelete(sql = "UPDATE ubicacion_tarea SET deleted = 1 WHERE ubicacion_id = ? AND tarea_id = ?")
@Where(clause = "deleted = 0")
@IdClass(UbicacionTareaId.class)
public class UbicacionTarea implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @Id
    @ManyToOne
    @JoinColumn(name = "tarea_id")
    private Tarea tarea;

    @Column(name = "deleted", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private boolean deleted = false;

    // Constructores, getters y setters omitidos por brevedad

    public UbicacionTarea() {
    }

    public UbicacionTarea(Ubicacion ubicacion, Tarea tarea) {
        this.ubicacion = ubicacion;
        this.tarea = tarea;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
