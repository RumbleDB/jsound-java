package jsound.types;


public enum AtomicTypes {
    ANYURI("anyURI"),

    STRING("string"),

    INTEGER("integer"),
    DECIMAL("decimal"),
    DOUBLE("double"),

    BOOLEAN("boolean"),

    DATETIME("dateTime"),
    DATE("date"),
    TIME("time"),

    DURATION("duration"),
    YEARMONTHDURATION("yearMonthDuration"),
    DAYTIMEDURATION("dayTimeDuration"),

    HEXBINARY("hexBinary"),
    BASE64BINARY("base64Binary"),

    NULL("null");

    private String typeName;

    AtomicTypes(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
