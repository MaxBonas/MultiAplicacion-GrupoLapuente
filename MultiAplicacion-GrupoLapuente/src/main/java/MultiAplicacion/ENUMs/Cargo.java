package MultiAplicacion.ENUMs;

public enum Cargo {
    OPERARIO,
    MANTENIMIENTO,
    JEFE_DE_EQUIPO,
    RESPONSABLE_DE_ALMACEN,
    RESPONSABLE_DE_LOGISTICA,
    RESPONSABLE_DE_PRODUCCION;

    @Override
    public String toString() {
        String s = super.toString();
        s = s.replace('_', ' ').toLowerCase();

        // Capitalize the first letter of each word
        String[] words = s.split(" ");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1);
        }

        return String.join(" ", words);
    }
}
