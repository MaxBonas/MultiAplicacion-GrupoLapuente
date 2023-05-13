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
@SQLDelete(sql = "UPDATE ubicaciones SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "This field can't be blank")
    @NotNull(message = "This field can't be null")
    private String name;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "ubicacion_tarea",
            joinColumns = @JoinColumn(name = "ubicacion_id"),
            inverseJoinColumns = @JoinColumn(name = "tarea_id"))
    private Set<Tarea> tareas = new HashSet<>();

    @OneToMany(mappedBy = "ubicacion")
    @JsonIgnore
    private Set<TareaCumplida> tareasCumplidas = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "sociedad_id", nullable = false)
    private Sociedad sociedad;
    @Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = false;

    public Ubicacion() {
    }

    public Ubicacion(String name, Sociedad sociedad) {
        this.name = name;
        this.sociedad = sociedad;
    }

    public Ubicacion(Long id, String name, Set<Tarea> tareas, Set<TareaCumplida> tareasCumplidas, Sociedad sociedad) {
        this.id = id;
        this.name = name;
        this.tareas = tareas;
        this.tareasCumplidas = tareasCumplidas;
        this.sociedad = sociedad;
    }

    public Ubicacion(String name, Set<Tarea> tareas, Set<TareaCumplida> tareasCumplidas, Sociedad sociedad) {
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

    public Set<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(Set<Tarea> tareas) {
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