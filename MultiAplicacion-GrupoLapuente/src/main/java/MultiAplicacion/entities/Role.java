package MultiAplicacion.entities;


import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@SQLDelete(sql = "UPDATE role SET deleted = 1 WHERE id = ?")
@Where(clause = "deleted = 0")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String role;
    @Column(name = "deleted", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Role(String role, User user) {
        this.role = role;
        this.user = user;
    }

    public Role() {
    }

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
