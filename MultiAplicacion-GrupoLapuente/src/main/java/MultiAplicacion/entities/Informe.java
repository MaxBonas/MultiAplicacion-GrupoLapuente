package MultiAplicacion.entities;

import MultiAplicacion.DTOs.InformeDTO;
import MultiAplicacion.ENUMs.Turno;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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




    // Constructor vacío
    public Informe() {
    }

    // Constructor con parámetros
    public Informe(LocalDateTime fecha, Ubicacion ubicacion, Turno turno) {
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.turno = turno;
    }

    public Informe(Long id, LocalDateTime fecha, Ubicacion ubicacion, Turno turno) {
        this.id = id;
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.turno = turno;
    }

    public Informe(InformeDTO informeDTO, Ubicacion ubicacion) {
        this.fecha = informeDTO.getFecha();
        this.ubicacion = ubicacion;
        this.turno = informeDTO.getTurno();
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
}
