package MultiAplicacion.DTOs;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class UbicacionDTO {
    private Long id;
    @NotNull(message = "El nombre no puede ser nulo")
    @NotEmpty(message = "El nombre no puede estar vacío")
    private String name;
    private Set<Long> tareasIds;

    private Long sociedadId;

    // Constructor, getters y setters

    public UbicacionDTO() {
    }

    public UbicacionDTO(String name, Long sociedadId) {
        this.name = name;
        this.sociedadId = sociedadId;
    }

    public UbicacionDTO(String name, Set<Long> tareasIds, Long sociedadId) {
        this.name = name;
        this.tareasIds = tareasIds;
        this.sociedadId = sociedadId;
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

    public Long getSociedadId() {
        return sociedadId;
    }

    public void setSociedadId(Long sociedadId) {
        this.sociedadId = sociedadId;
    }
}

