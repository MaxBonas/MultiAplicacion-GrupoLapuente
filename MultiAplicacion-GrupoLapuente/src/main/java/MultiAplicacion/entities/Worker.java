package MultiAplicacion.entities;

import MultiAplicacion.ENUMs.Cargo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
// La anotación @Entity indica que esta clase es una entidad y se mapeará a una tabla en la base de datos.
public class Worker extends User { // La clase Worker hereda de la clase User.

    // La anotación @Enumerated se utiliza para indicar que el tipo de la columna de la base de datos será de tipo String y tomará los valores del enum Cargo.
    @Enumerated(EnumType.STRING)
    private Cargo cargo;

    // La anotación @OneToMany indica que existe una relación de uno a muchos entre las entidades Worker y TareaCumplida.
    @OneToMany(mappedBy = "worker")
    @JsonIgnore
    private Set<TareaCumplida> tareasCumplidas = new HashSet<>();

    // Constructor por defecto de la clase Worker
    public Worker() {}

    // Constructores de la clase Worker con parámetros
    // Estos constructores inicializan un objeto Worker con los atributos específicos
    public Worker(String name, String password, Cargo cargo, Sociedad sociedad) {
        super(name, password, sociedad); // Llamada al constructor de la clase padre User.
        this.cargo = cargo;
    }

    public Worker(Long id, String name, String password, Cargo cargo, Set<TareaCumplida> tareasCumplidas, Sociedad sociedad) {
        super(id, name, password, sociedad); // Llamada al constructor de la clase padre User.
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