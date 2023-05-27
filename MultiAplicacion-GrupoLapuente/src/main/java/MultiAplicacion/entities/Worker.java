package MultiAplicacion.entities;

import MultiAplicacion.ENUMs.Cargo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
public class Worker extends User {

    @Enumerated(EnumType.STRING)
    private Cargo cargo;

    @OneToMany(mappedBy = "worker")
    @JsonIgnore
    private Set<TareaCumplida> tareasCumplidas = new HashSet<>();
    @ManyToMany(mappedBy = "workers")
    @JsonIgnore
    private Set<Informe> informes = new HashSet<>();


    public Worker() {}

    public Worker(String name, String password, Cargo cargo, Sociedad sociedad) {
        super(name, password, sociedad);
        this.cargo = cargo;

    }

    public Worker(Long id, String name, String password, Cargo cargo, Set<TareaCumplida> tareasCumplidas, Sociedad sociedad) {
        super(id, name, password, sociedad);
        this.cargo = cargo;
        this.tareasCumplidas = tareasCumplidas;
    }

    public Worker(Cargo cargo, Set<TareaCumplida> tareasCumplidas) {
        this.cargo = cargo;
        this.tareasCumplidas = tareasCumplidas;
    }

    // Getters and setters

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Set<TareaCumplida> getTareasCumplidas() {
        return tareasCumplidas;
    }

    public void setTareasCumplidas(Set<TareaCumplida> tareasCumplidas) {
        this.tareasCumplidas = tareasCumplidas;
    }

}