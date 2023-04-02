package Lapuente.TareasUbicaciones.DTOs;

import javax.persistence.Column;

public class TareaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long ubicacionId;

    // Constructor, getters y setters


    public TareaDTO(String nombre, String descripcion, Long ubicacionId) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacionId = ubicacionId;
    }





    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getUbicacionId() {
        return ubicacionId;
    }

    public void setUbicacionId(Long ubicacionId) {
        this.ubicacionId = ubicacionId;
    }
}

