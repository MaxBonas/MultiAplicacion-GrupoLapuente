package MultiAplicacion.DTOs;

import java.util.Set;

public class TareaDTO {
    private Long id;
    private String name;
    private String descripcion;
    private Set<Long> ubicacionesIds;

    // Constructor, getters y setters

    public TareaDTO() {
    }

    public TareaDTO(String name, String descripcion, Set<Long> ubicacionesIds) {
        this.name = name;
        this.descripcion = descripcion;
        this.ubicacionesIds = ubicacionesIds;
    }

    public TareaDTO(String name, String descripcion) {
        this.name = name;
        this.descripcion = descripcion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Long> getUbicacionesIds() {
        return ubicacionesIds;
    }

    public void setUbicacionesIds(Set<Long> ubicacionesIds) {
        this.ubicacionesIds = ubicacionesIds;
    }
}

