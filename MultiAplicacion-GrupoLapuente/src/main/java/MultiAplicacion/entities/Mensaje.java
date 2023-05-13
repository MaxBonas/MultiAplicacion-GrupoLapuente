package MultiAplicacion.entities;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@SQLDelete(sql = "UPDATE mensaje SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emisor_id", nullable = false)
    private User emisor;

    private boolean activo;

    @ManyToOne
    @JoinColumn(name = "receptor_id", nullable = false)
    private User receptor;

    @Column(nullable = false)
    private String asunto;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private boolean leido;

    @Column(nullable = false)
    private boolean circular;
    @Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = false;

    public Mensaje() {
    }

    public Mensaje(User emisor, User receptor, String asunto, String contenido, LocalDateTime fechaHora, boolean leido, boolean circular, boolean activo) {
        this.emisor = emisor;
        this.receptor = receptor;
        this.asunto = asunto;
        this.contenido = contenido;
        this.fechaHora = fechaHora;
        this.leido = leido;
        this.circular = circular;
        this.activo = activo;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getEmisor() {
        return emisor;
    }

    public void setEmisor(User emisor) {
        this.emisor = emisor;
    }

    public User getReceptor() {
        return receptor;
    }

    public void setReceptor(User receptor) {
        this.receptor = receptor;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public boolean isCircular() {
        return circular;
    }

    public void setCircular(boolean circular) {
        this.circular = circular;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
