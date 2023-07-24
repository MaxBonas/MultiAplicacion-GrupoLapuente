package MultiAplicacion.entities;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

// La anotación @Entity indica que esta clase es una entidad que se mapeará con una tabla en la base de datos
@Entity
public class Admin extends MultiAplicacion.entities.User {

    // La anotación @NotBlank se usa para validar que el campo email no esté vacío o nulo
    @NotBlank(message = "Este campo no puede estar vacío")
    private String email;

    // Constructor por defecto de la clase Admin
    public Admin() {
    }

    // Constructor de la clase Admin con parámetros
    // Este constructor inicializa un objeto Admin con un nombre, email, contraseña y sociedad específicos
    // También agrega un nuevo rol de "ADMIN" al objeto Admin creado
    public Admin(String name, String email, String password, MultiAplicacion.entities.Sociedad sociedad) {
        super(name, password, sociedad);
        this.email = email;
        getRoles().add(new MultiAplicacion.entities.Role("ADMIN", this));
    }

    // Método getter para obtener el valor del campo email
    public String getEmail() {
        return email;
    }

    // Método setter para establecer el valor del campo email
    public void setEmail(String email) {
        this.email = email;
    }
}
