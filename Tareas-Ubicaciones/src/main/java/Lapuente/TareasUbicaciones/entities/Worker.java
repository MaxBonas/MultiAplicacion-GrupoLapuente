package Lapuente.TareasUbicaciones.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Worker extends User{

    private String cargo;

    @OneToMany(mappedBy = "worker")
    @JsonIgnore
    private Set<Tarea> tareas = new HashSet<>();

    @OneToMany(mappedBy = "worker")
    private TareasDiarias tareasDiarias = new ArrayList<>();

    public Worker() {}

    public Worker(String name, String password, String cargo) {
        super(name, password);
        this.cargo = cargo;
    }

// Getters and setters

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Set<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(Set<Tarea> tareas) {
        this.tareas = tareas;
    }

    public List<TareasDiarias> getTareasDiarias() {
        return tareasDiarias;
    }

    public void setTareasDiarias(List<TareasDiarias> tareasDiarias) {
        this.tareasDiarias = tareasDiarias;
    }

    public Thread getUbicacion() {
    }
}
