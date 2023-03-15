package Lapuente.TareasUbicaciones.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
public class Admin extends User{

    @NotBlank(message = "This field can't be blank")
    @NotNull(message = "This field can't be null")
    private String email;

    public Admin() {
    }

    public Admin(String name, String email, String password) {
        super(name, password);
        this.email = email;
        getRoles().add(new Role("ADMIN", this));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
