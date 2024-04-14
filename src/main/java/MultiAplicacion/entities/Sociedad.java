package MultiAplicacion.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

// La anotación @Entity indica que esta clase es una entidad que se mapeará con una tabla en la base de datos
@Entity
// La anotación @SQLDelete se usa para modificar el comportamiento de la operación de eliminación. En este caso, en lugar de eliminar registros, se marca como eliminado.
@SQLDelete(sql = "UPDATE sociedad SET deleted = 1 WHERE id = ?")
// La anotación @Where se utiliza para filtrar los registros que tienen el campo "deleted" igual a 0 (no eliminados).
@Where(clause = "deleted = 0")
public class Sociedad {

    // La anotación @Id y @GeneratedValue se utilizan para auto-generar la clave primaria de la tabla.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // La anotación @NotBlank se usa para validar que el campo name no esté vacío o nulo
    @NotBlank(message = "Este campo no puede estar vacío")
    private String name;

    // La anotación @OneToMany indica que existe una relación de uno a muchos entre la entidad Sociedad y la entidad User.
    @OneToMany(mappedBy = "sociedad")
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "sociedad")
    private Set<Ubicacion> ubicaciones = new HashSet<>();

    @Column(name = "deleted", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private boolean deleted = false;

    // Constructor por defecto de la clase Sociedad
    public Sociedad() {
    }

    // Constructor de la clase Sociedad con parámetros
    // Este constructor inicializa un objeto Sociedad con un nombre específico
    public Sociedad(String name) {
        this.name = name;
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
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}