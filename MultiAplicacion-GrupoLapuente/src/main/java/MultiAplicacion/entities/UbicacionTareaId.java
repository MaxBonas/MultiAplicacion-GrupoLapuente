package MultiAplicacion.entities;

import java.io.Serializable;
import java.util.Objects;

public class UbicacionTareaId implements Serializable {
    private Long ubicacion;
    private Long tarea;

    // Constructores, getters y setters omitidos por brevedad

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UbicacionTareaId)) return false;
        UbicacionTareaId that = (UbicacionTareaId) o;
        return Objects.equals(ubicacion, that.ubicacion) &&
                Objects.equals(tarea, that.tarea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ubicacion, tarea);
    }
}