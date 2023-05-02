package MultiAplicacion.DTOs;

import MultiAplicacion.entities.Role;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class WorkerDTO {
    private Long id;
    @NotNull(message = "El nombre no puede ser nulo")
    @NotEmpty(message = "El nombre no puede estar vacío")
    private String name;
    private String password;
    private String cargo;

    private Long sociedadId;


    // Constructor, getters y setters

    public WorkerDTO(Long id, String name, String password, String cargo, Set<Role> roles, Long sociedadId) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.cargo = cargo;
        this.sociedadId = sociedadId;
    }

    public WorkerDTO() {
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Long getSociedadId() {
        return sociedadId;
    }

    public void setSociedadId(Long sociedadId) {
        this.sociedadId = sociedadId;
    }
}


