package Lapuente.TareasUbicaciones.DTOs;

import java.util.Set;

public class UbicacionDTO {
    private Long id;
    private String name;
    private Set<Long> tareasIds;

    // Constructor, getters y setters
    public UbicacionDTO(String name) {
        this.name = name;
    }

    public UbicacionDTO(String name, Set<Long> tareasIds) {
        this.name = name;
        this.tareasIds = tareasIds;
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

    public Set<Long> getTareasIds() {
        return tareasIds;
    }

    public void setTareasIds(Set<Long> tareasIds) {
        this.tareasIds = tareasIds;
    }
}

