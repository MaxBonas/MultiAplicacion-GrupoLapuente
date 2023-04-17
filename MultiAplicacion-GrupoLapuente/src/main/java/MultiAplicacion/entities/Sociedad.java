package MultiAplicacion.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Sociedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "This field can't be blank")
    private String name;

    @OneToMany(mappedBy = "sociedad")
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "sociedad")
    private Set<Ubicacion> ubicaciones = new HashSet<>();

    public Sociedad() {
    }

    public Sociedad(String name) {
        this.name = name;
    }

    // Getters y setters

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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Ubicacion> getUbicaciones() {
        return ubicaciones;
    }

    public void setUbicaciones(Set<Ubicacion> ubicaciones) {
        this.ubicaciones = ubicaciones;
    }
}