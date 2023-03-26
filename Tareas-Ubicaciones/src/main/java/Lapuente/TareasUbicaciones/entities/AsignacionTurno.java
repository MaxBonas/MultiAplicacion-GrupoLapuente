package Lapuente.TareasUbicaciones.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AsignacionTurno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    private LocalDateTime fechaInicioTurno;

    private LocalDateTime fechaFinTurno;

    public AsignacionTurno() {}

    public AsignacionTurno(Worker worker, Ubicacion ubicacion, LocalDateTime fechaInicioTurno, LocalDateTime fechaFinTurno) {
        this.worker = worker;
        this.ubicacion = ubicacion;
        this.fechaInicioTurno = fechaInicioTurno;
        this.fechaFinTurno = fechaFinTurno;
    }

    // SETTERS Y GETTERS
}
