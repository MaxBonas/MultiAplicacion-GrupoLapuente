package MultiAplicacion.DTOs;

import java.util.List;

public class MensajeDTO {
    private Long id;
    private Long emisorId;
    private List<Long> receptorIds;
    private String asunto;
    private String contenido;
    private boolean circular;
    private boolean activo;

    // Constructor, getters y setters

    public MensajeDTO() {
    }

    public MensajeDTO(Long emisorId, List<Long> receptorIds, String asunto, String contenido, boolean circular, boolean activo) {
        this.emisorId = emisorId;
        this.receptorIds = receptorIds;
        this.asunto = asunto;
        this.contenido = contenido;
        this.circular = circular;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmisorId() {
        return emisorId;
    }

    public void setEmisorId(Long emisorId) {
        this.emisorId = emisorId;
    }

    public List<Long> getReceptorIds() {
        return receptorIds;
    }

    public void setReceptorIds(List<Long> receptorIds) {
        this.receptorIds = receptorIds;
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
}
