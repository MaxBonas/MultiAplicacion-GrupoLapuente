package Lapuente.TareasUbicaciones.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ubicaciones")
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

    public Ubicacion() {
    }

    public Ubicacion(String name) {
        this.name = name;
    }

    public Ubicacion(String name, Set<Tarea> tareas, Set<TareaCumplida> tareasCumplidas) {
        this.name = name;
        this.tareas = tareas;
        this.tareasCumplidas = tareasCumplidas;
    }

    public Ubicacion(Long id, String name, Set<Tarea> tareas, Set<TareaCumplida> tareasCumplidas) {
        this.id = id;
        this.name = name;
        this.tareas = tareas;
        this.tareasCumplidas = tareasCumplidas;
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
}