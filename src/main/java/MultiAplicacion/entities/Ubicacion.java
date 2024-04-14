package MultiAplicacion.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "ubicaciones")
// La anotación @SQLDelete se usa para modificar el comportamiento de la operación de eliminación. En este caso, en lugar de eliminar registros, se marca como eliminado.
@SQLDelete(sql = "UPDATE ubicaciones SET deleted = 1 WHERE id = ?")
// La anotación @Where se utiliza para filtrar los registros que tienen el campo "deleted" igual a 0 (no eliminados).
@Where(clause = "deleted = 0")
public class Ubicacion {

    // La anotación @Id y @GeneratedValue se utilizan para auto-generar la clave primaria de la tabla.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Las anotaciones @NotBlank y @NotNull se usan para validar que el campo name no esté vacío o nulo
    @NotBlank(message = "Este campo no puede estar vacío")
    @NotNull(message = "Este campo no puede ser nulo")
    private String name;

    // La anotación @OneToMany indica que existe una relación de uno a muchos entre las entidades Ubicacion y UbicacionTarea.
    @OneToMany(mappedBy = "ubicacion")
    private Set<UbicacionTarea> tareas = new HashSet<>();

    @OneToMany(mappedBy = "ubicacion")
    @JsonIgnore
    private Set<TareaCumplida> tareasCumplidas = new HashSet<>();

    // La anotación @ManyToOne indica que existe una relación de muchos a uno entre la entidad Ubicacion y la entidad Sociedad.
    @ManyToOne
    @JoinColumn(name = "sociedad_id", nullable = false)
    private Sociedad sociedad;

    @Column(name = "deleted", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private boolean deleted = false;

    // Constructor por defecto de la clase Ubicacion
    public Ubicacion() {
    }

    // Constructores de la clase Ubicacion con parámetros
    // Estos constructores inicializan un objeto Ubicacion con los atributos específicos
    public Ubicacion(String name, Sociedad sociedad) {
        this.name = name;
        this.sociedad = sociedad;
    }

    public Ubicacion(Long id, String name, Set<UbicacionTarea> tareas, Set<TareaCumplida> tareasCumplidas, Sociedad sociedad) {
        this.id = id;
        this.name = name;
        this.tareas = tareas;
        this.tareasCumplidas = tareasCumplidas;
        this.sociedad = sociedad;
    }

    public Ubicacion(String name, Set<UbicacionTarea> tareas, Set<TareaCumplida> tareasCumplidas, Sociedad sociedad) {
        this.name = name;
        this.tareas = tareas;
        this.tareasCumplidas = tareasCumplidas;
        this.sociedad = sociedad;
    }
    // Getters and setters

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

    public Set<UbicacionTarea> getTareas() {
        return tareas;
    }

    public void setTareas(Set<UbicacionTarea> tareas) {
        this.tareas = tareas;
    }

    public Set<TareaCumplida> getTareasCumplidas() {
        return tareasCumplidas;
    }

    public void setTareasCumplidas(Set<TareaCumplida> tareasCumplidas) {
        this.tareasCumplidas = tareasCumplidas;
    }

    public Sociedad getSociedad() {
        return sociedad;
    }

    public void setSociedad(Sociedad sociedad) {
        this.sociedad = sociedad;
    }
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}