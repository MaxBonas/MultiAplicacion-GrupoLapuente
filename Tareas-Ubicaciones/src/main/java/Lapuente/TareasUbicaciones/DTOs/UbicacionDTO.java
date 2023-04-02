package Lapuente.TareasUbicaciones.DTOs;

public class UbicacionDTO {
    private Long id;
    private String name;

    // Constructor, getters y setters

    public UbicacionDTO(Long id, String name) {
        this.id = id;
        this.name = name;
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
}

