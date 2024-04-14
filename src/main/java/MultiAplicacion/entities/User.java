package MultiAplicacion.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import java.time.LocalDate;

@Entity
@Table(name = "users")
// La anotación @SQLDelete se usa para modificar el comportamiento de la operación de eliminación. En este caso, en lugar de eliminar registros, se marca como eliminado.
@SQLDelete(sql = "UPDATE users SET deleted = 1 WHERE id = ?")
// La anotación @Where se utiliza para filtrar los registros que tienen el campo "deleted" igual a 0 (no eliminados).
@Where(clause = "deleted = 0")
public abstract class User {

    // La anotación @Id y @GeneratedValue se utilizan para auto-generar la clave primaria de la tabla.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Las anotaciones @Column, @NotBlank y @NotNull se usan para validar que el campo name no esté vacío o nulo y para indicar que es un campo único en la base de datos.
    @Column(unique = true, nullable = false, length = 191)
    @NotBlank(message = "Este campo no puede estar vacío")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Este campo no puede estar vacío")
    private String password;

    // La anotación @OneToMany indica que existe una relación de uno a muchos entre las entidades User y Role.
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Role> roles = new HashSet<>(); // Un Set es una lista sin duplicados

    // La anotación @ManyToOne indica que existe una relación de muchos a uno entre la entidad User y la entidad Sociedad.
    @ManyToOne
    @JoinColumn(name = "sociedad_id", nullable = false)
    private Sociedad sociedad;

    @OneToMany(mappedBy = "emisor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Mensaje> mensajesEnviados = new HashSet<>();

    @OneToMany(mappedBy = "receptor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Mensaje> mensajesRecibidos = new HashSet<>();

    @Column(name = "deleted", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private boolean deleted = false;

    // Constructores de la clase User con parámetros
    // Estos constructores inicializan un objeto User con los atributos específicos
    public User(Long id, String name, String password, Sociedad sociedad) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.sociedad = sociedad;
    }

    public User(String name, String password, Sociedad sociedad) {
        this.name = name;
        this.password = password;
        this.sociedad = sociedad;
    }

    public User(String name, String password, Set<Role> roles, Sociedad sociedad, Set<Mensaje> mensajesEnviados, Set<Mensaje> mensajesRecibidos) {
        this.name = name;
        this.password = password;
        this.roles = roles;
        this.sociedad = sociedad;
        this.mensajesEnviados = mensajesEnviados;
        this.mensajesRecibidos = mensajesRecibidos;
    }

    // Constructor por defecto de la clase User
    public User() {
    }

    // Getters and setters

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sociedad getSociedad() {
        return sociedad;
    }

    public void setSociedad(Sociedad sociedad) {
        this.sociedad = sociedad;
    }

    public Set<Mensaje> getMensajesEnviados() {
        return mensajesEnviados;
    }

    public void setMensajesEnviados(Set<Mensaje> mensajesEnviados) {
        this.mensajesEnviados = mensajesEnviados;
    }

    public Set<Mensaje> getMensajesRecibidos() {
        return mensajesRecibidos;
    }

    public void setMensajesRecibidos(Set<Mensaje> mensajesRecibidos) {
        this.mensajesRecibidos = mensajesRecibidos;
    }
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}