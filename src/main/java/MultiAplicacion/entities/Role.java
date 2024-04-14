package MultiAplicacion.entities;


import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
// La anotación @Entity indica que esta clase es una entidad que se mapeará con una tabla en la base de datos
@Entity
// La anotación @SQLDelete se usa para modificar el comportamiento de la operación de eliminación. En este caso, en lugar de eliminar registros, se marca como eliminado.
@SQLDelete(sql = "UPDATE role SET deleted = 1 WHERE id = ?")
// La anotación @Where se utiliza para filtrar los registros que tienen el campo "deleted" igual a 0 (no eliminados).
@Where(clause = "deleted = 0")
public class Role {

    // La anotación @Id y @GeneratedValue se utilizan para auto-generar la clave primaria de la tabla.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

    @Column(name = "deleted", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private boolean deleted = false;

    // La anotación @ManyToOne indica que existe una relación de muchos a uno entre la entidad Role y la entidad User.
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Constructor de la clase Role con parámetros
    // Este constructor inicializa un objeto Role con un rol y un usuario específicos
    public Role(String role, User user) {
        this.role = role;
        this.user = user;
    }

    // Constructor por defecto de la clase Role
    public Role() {
    }

    // Métodos getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
