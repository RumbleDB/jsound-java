package org.jsound.type;

public enum Kinds {
    OBJECT("object"),
    ARRAY("array"),
    ATOMIC("atomic"),
    UNION("union");

    private String typeName;

    Kinds(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
