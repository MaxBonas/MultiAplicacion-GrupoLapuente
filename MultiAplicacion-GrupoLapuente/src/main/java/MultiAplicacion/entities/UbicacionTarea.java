package MultiAplicacion.entities;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ubicacion_tarea")
// La anotación @SQLDelete se usa para modificar el comportamiento de la operación de eliminación. En este caso, en lugar de eliminar registros, se marca como eliminado.
@SQLDelete(sql = "UPDATE ubicacion_tarea SET deleted = 1 WHERE ubicacion_id = ? AND tarea_id = ?")
// La anotación @Where se utiliza para filtrar los registros que tienen el campo "deleted" igual a 0 (no eliminados).
@Where(clause = "deleted = 0")
// La anotación @IdClass se utiliza para especificar una clase que contiene los campos de clave principal de la entidad.
@IdClass(UbicacionTareaId.class)
public class UbicacionTarea implements Serializable {

    // La anotación @Id junto con @ManyToOne y @JoinColumn se utilizan para establecer las claves primarias y las relaciones de muchos a uno con las entidades Ubicacion y Tarea.
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

    // Constructor por defecto de la clase UbicacionTarea
    public UbicacionTarea() {
    }

    // Constructor de la clase UbicacionTarea con parámetros
    // Este constructor inicializa un objeto UbicacionTarea con una ubicación y tarea específicas
    public UbicacionTarea(Ubicacion ubicacion, Tarea tarea) {
        this.ubicacion = ubicacion;
        this.tarea = tarea;
    }

    // Getters and setters

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
