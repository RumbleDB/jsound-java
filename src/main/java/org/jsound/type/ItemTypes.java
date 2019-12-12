package org.jsound.type;

public enum ItemTypes {
    ATOMIC("atomic"),

    VALUE("value"),

    OBJECT("object"),
    ARRAY("array"),
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

    ItemTypes() {
    }

    ItemTypes(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String name) {
        this.typeName = name;
    }
}
