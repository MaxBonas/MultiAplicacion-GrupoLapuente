package Lapuente.TareasUbicaciones.entities;

import Lapuente.TareasUbicaciones.DTOs.InformeDTO;
import Lapuente.TareasUbicaciones.DTOs.TareaCumplidaDTO;
import Lapuente.TareasUbicaciones.ENUMs.Turno;
import Lapuente.TareasUbicaciones.repositories.TareaCumplidaRepository;
import Lapuente.TareasUbicaciones.repositories.TareaRepository;
import Lapuente.TareasUbicaciones.repositories.UbicacionRepository;
import Lapuente.TareasUbicaciones.repositories.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Informe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id", nullable = false)
    private Ubicacion ubicacion;

    @Enumerated(EnumType.STRING)
    private Turno turno;

    @OneToMany(mappedBy = "informe", cascade = CascadeType.ALL)
    private List<TareaCumplida> tareasCumplidas;


    private String comentario;

    // Constructor vacío
    public Informe() {
    }

    // Constructor con parámetros
    public Informe(LocalDateTime fecha, Ubicacion ubicacion, Turno turno, String comentario) {
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.turno = turno;
        this.comentario = comentario;
    }

    public Informe(Long id, LocalDateTime fecha, Ubicacion ubicacion, Turno turno, String comentario) {
        this.id = id;
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.turno = turno;
        this.comentario = comentario;
    }

    public Informe(InformeDTO informeDTO, Ubicacion ubicacion) {
        this.fecha = informeDTO.getFecha();
        this.ubicacion = ubicacion;
        this.turno = informeDTO.getTurno();
        this.comentario = informeDTO.getComentario();
        this.tareasCumplidas = new ArrayList<>();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public List<TareaCumplida> getTareasCumplidas() {
        return tareasCumplidas;
    }

    public void setTareasCumplidas(List<TareaCumplida> tareasCumplidas) {
        this.tareasCumplidas = tareasCumplidas;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

}
