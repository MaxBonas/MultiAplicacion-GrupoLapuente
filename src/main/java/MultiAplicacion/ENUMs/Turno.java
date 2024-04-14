package MultiAplicacion.ENUMs;

public enum Turno {
    MANANA("MAÑANA"),
    TARDE("TARDE");
    //NOCHE("NOCHE");

    private String value;

    Turno(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Turno fromString(String value) {
        for (Turno turno : Turno.values()) {
            if (turno.value.equalsIgnoreCase(value)) {
                return turno;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}
