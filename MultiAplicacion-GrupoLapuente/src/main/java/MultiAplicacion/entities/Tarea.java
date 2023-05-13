package MultiAplicacion.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "tareas")
@SQLDelete(sql = "UPDATE tareas SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "This field can't be blank")
    @NotNull(message = "This field can't be null")
    private String name;

    @ManyToMany(mappedBy = "tareas")
    private Set<Ubicacion> ubicaciones = new HashSet<>();

    @NotBlank(message = "This field can't be blank")
    @NotNull(message = "This field can't be null")
    private String descripcion;
    @Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = false;

    public Tarea() {}

    public Tarea(String name, String descripcion) {
        this.name = name;
        this.descripcion = descripcion;
    }

    public Tarea(String name, String descripcion, Set<Ubicacion> ubicaciones) {
        this.name = name;
        this.ubicaciones = ubicaciones;
        this.descripcion = descripcion;
    }

    public Tarea(Long id, String name, Set<Ubicacion> ubicaciones, String descripcion) {
        this.id = id;
        this.name = name;
        this.ubicaciones = ubicaciones;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Ubicacion> getUbicaciones() {
        return ubicaciones;
    }

    public void setUbicaciones(Set<Ubicacion> ubicaciones) {
        this.ubicaciones = ubicaciones;
    }
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
