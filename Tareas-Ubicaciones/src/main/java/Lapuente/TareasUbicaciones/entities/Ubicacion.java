package Lapuente.TareasUbicaciones.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ubicaciones")
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "This field can't be blank")
    @NotNull(message = "This field can't be null")
    private String nombre;

    @ManyToMany(mappedBy = "ubicaciones")
    @JsonIgnore
    private Set<Tarea> tareas = new HashSet<>();

    public Ubicacion() {}

    public Ubicacion(String nombre) {
        this.nombre = nombre;
    }

}