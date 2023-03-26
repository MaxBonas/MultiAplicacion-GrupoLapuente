package Lapuente.TareasUbicaciones.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TareaCumplida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarea_id")
    private Tarea tarea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private Worker worker;

    private boolean cumplida;

    private LocalDateTime fechaCumplimiento;

    public TareaCumplida() {}

    public TareaCumplida(Tarea tarea, Worker worker, boolean cumplida, LocalDateTime fechaCumplimiento) {
        this.tarea = tarea;
        this.worker = worker;
        this.cumplida = cumplida;
        this.fechaCumplimiento = fechaCumplimiento;
    }

    // SETTERS Y GETTERS
}