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
// La anotación @SQLDelete se usa para modificar el comportamiento de la operación de eliminación. En este caso, en lugar de eliminar registros, se marca como eliminado.
@SQLDelete(sql = "UPDATE tareas SET deleted = 1 WHERE id = ?")
// La anotación @Where se utiliza para filtrar los registros que tienen el campo "deleted" igual a 0 (no eliminados).
@Where(clause = "deleted = 0")
public class Tarea {

    // La anotación @Id y @GeneratedValue se utilizan para auto-generar la clave primaria de la tabla.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Las anotaciones @NotBlank y @NotNull se usan para validar que el campo name no esté vacío o nulo
    @NotBlank(message = "Este campo no puede estar vacío")
    @NotNull(message = "Este campo no puede ser nulo")
    private String name;

    // La anotación @OneToMany indica que existe una relación de uno a muchos entre la entidad Tarea y la entidad UbicacionTarea.
    @OneToMany(mappedBy = "tarea")
    private Set<UbicacionTarea> ubicaciones = new HashSet<>();

    @NotBlank(message = "Este campo no puede estar vacío")
    @NotNull(message = "Este campo no puede ser nulo")
    private String descripcion;

    @Column(name = "deleted", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private boolean deleted = false;

    // Constructor por defecto de la clase Tarea
    public Tarea() {}

    // Constructor de la clase Tarea con parámetros
    // Estos constructores inicializan un objeto Tarea con un nombre, descripción y ubicaciones específicas
    public Tarea(String name, String descripcion) {
        this.name = name;
        this.descripcion = descripcion;
    }

    public Tarea(String name, String descripcion, Set<UbicacionTarea> ubicaciones) {
        this.name = name;
        this.ubicaciones = ubicaciones;
        this.descripcion = descripcion;
    }

    public Tarea(Long id, String name, Set<UbicacionTarea> ubicaciones, String descripcion) {
        this.id = id;
        this.name = name;
        this.ubicaciones = ubicaciones;
        this.descripcion = descripcion;
    }

    // Métodos getters y setters

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

    public Set<UbicacionTarea> getUbicaciones() {
        return ubicaciones;
    }

    public void setUbicaciones(Set<UbicacionTarea> ubicaciones) {
        this.ubicaciones = ubicaciones;
    }
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
