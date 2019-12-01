package org.jsound.type;

public class ObjectKey {

    private String _typeStringValue;
    private boolean required, unique, allowNull;

    public ObjectKey(String typeString) {
        this._typeStringValue = this.setMarkers(typeString);
    }
    private String setMarkers(String typeString) {
        this.required = typeString.contains("!");
        this.unique = typeString.contains("@");
        this.allowNull = typeString.contains("?");
        return typeString
                .replace("!", "")
                .replace("@", "")
                .replace("?", "");
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isUnique() {
        return unique;
    }

    public boolean allowsNull() {
        return allowNull;
    }
}
