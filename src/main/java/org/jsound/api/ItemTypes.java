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

    HEXBINARY("hexBinary"),
    BASE64BINARY("base64Binary"),

    DURATION("duration"),

    NULL("null");

    private final String typeName;

    ItemTypes(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
