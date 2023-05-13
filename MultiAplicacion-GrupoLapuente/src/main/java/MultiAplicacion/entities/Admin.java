package MultiAplicacion.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@Entity
public class Admin extends MultiAplicacion.entities.User {

    @NotBlank(message = "This field can't be blank")
    private String email;

    public Admin() {
    }

    public Admin(String name, String email, String password, MultiAplicacion.entities.Sociedad sociedad) {
        super(name, password, sociedad);
        this.email = email;
        getRoles().add(new MultiAplicacion.entities.Role("ADMIN", this));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
