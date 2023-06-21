package MultiAplicacion.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted = 1 WHERE id = ?")
@Where(clause = "deleted = 0")
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 191)
    @NotBlank(message = "This field can't be blank")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "This field can't be blank")
    private String password;


    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Role> roles = new HashSet<>(); // A Set is a List without duplicates

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

    public User() {
    }

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