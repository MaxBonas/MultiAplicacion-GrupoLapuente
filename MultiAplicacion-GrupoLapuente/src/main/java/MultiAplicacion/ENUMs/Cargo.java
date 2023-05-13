package MultiAplicacion.ENUMs;

public enum Cargo {
    OPERARIO,
    MANTENIMIENTO,
    SOLDADOR,
    CALIDAD,
    JEFE_DE_TURNO,
    JEFE_DE_MAQUINAS,
    JEFE_DE_MANTENIMIENTO,
    JEFE_DE_OPERACIONES,
    JEFE_DE_PRODUCCION,
    JEFE_DE_CALIDAD,
    JEFE_DE_SEGURIDAD;

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
