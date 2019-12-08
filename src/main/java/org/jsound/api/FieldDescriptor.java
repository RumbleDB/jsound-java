package org.jsound.api;

public class FieldDescriptor {
    private String name;
    private TypeDescriptor type;
    private String stringType;
    private Boolean required = false;
    private Item defaultValue;
    private Boolean unique = false;

    public void setName(String name) {
        this.name = name;
    }

    public void setType(TypeDescriptor type) {
        this.type = type;
    }

    public void setStringType(String stringType) {
        this.stringType = stringType;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public void setDefaultValue(Item defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }
}
