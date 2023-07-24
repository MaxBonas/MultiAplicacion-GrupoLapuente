package MultiAplicacion.entities;

import java.io.Serializable;
import java.util.Objects;

public class UbicacionTareaId implements Serializable {
    // Estos campos representan los identificadores de las entidades Ubicacion y Tarea
    private Long ubicacion;
    private Long tarea;

    // Constructores, getters y setters omitidos por brevedad

    // El método equals se sobrescribe para proporcionar una implementación que compare la igualdad de las instancias de UbicacionTareaId en base a los identificadores de ubicacion y tarea.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UbicacionTareaId)) return false;
        UbicacionTareaId that = (UbicacionTareaId) o;
        return Objects.equals(ubicacion, that.ubicacion) &&
                Objects.equals(tarea, that.tarea);
    }

    // El método hashCode se sobrescribe para proporcionar una implementación que retorne un hash en base a los identificadores de ubicacion y tarea.
    @Override
    public int hashCode() {
        return Objects.hash(ubicacion, tarea);
    }
}
