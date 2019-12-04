package org.jsound.type;

public class ObjectKey {

    private String _typeStringValue;
    private boolean required, unique, allowNull;

    public ObjectKey(String typeString, boolean hasMarkers) {
        this._typeStringValue = hasMarkers ? this.setMarkers(typeString) : typeString;
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

    public String getKeyName() {
        return this._typeStringValue;
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

    @Override
    public boolean equals(Object o) {
        if (o instanceof ObjectKey) {
            ObjectKey objectKey = (ObjectKey) o;
            return this._typeStringValue.equals(objectKey.getKeyName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this._typeStringValue.hashCode();
    }
}
