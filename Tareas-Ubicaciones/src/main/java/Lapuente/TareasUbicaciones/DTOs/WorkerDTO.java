package Lapuente.TareasUbicaciones.DTOs;

import Lapuente.TareasUbicaciones.entities.Role;

import java.util.List;
import java.util.Set;

public class WorkerDTO {
    private Long id;
    private String name;
    private String password;
    private String cargo;

    // Constructor, getters y setters

    public WorkerDTO(Long id, String name, String password, String cargo, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.cargo = cargo;
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

}


