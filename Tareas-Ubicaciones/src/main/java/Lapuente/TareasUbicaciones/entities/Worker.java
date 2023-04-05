package Lapuente.TareasUbicaciones.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Worker extends User {

    private String cargo;

    @OneToMany(mappedBy = "worker")
    @JsonIgnore
    private Set<TareaCumplida> tareasCumplidas = new HashSet<>();

    public Worker() {}

    public Worker(String name, String password, String cargo) {
        super(name, password);
        this.cargo = cargo;
    }

    public Worker(Long id, String name, String password, String cargo, Set<TareaCumplida> tareasCumplidas) {
        super(id, name, password);
        this.cargo = cargo;
        this.tareasCumplidas = tareasCumplidas;
    }

    public Worker(String cargo, Set<TareaCumplida> tareasCumplidas) {
        this.cargo = cargo;
        this.tareasCumplidas = tareasCumplidas;
    }
    // Getters and setters

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Set<TareaCumplida> getTareasCumplidas() {
        return tareasCumplidas;
    }

    public void setTareasCumplidas(Set<TareaCumplida> tareasCumplidas) {
        this.tareasCumplidas = tareasCumplidas;
    }

}
