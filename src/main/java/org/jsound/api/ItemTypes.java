package org.jsound.api;

public enum ItemTypes {
    OBJECT("object"),
    ARRAY("array"),
    ANY_URI("anyURI"),

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

    private final String typeName;

    ItemTypes(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
