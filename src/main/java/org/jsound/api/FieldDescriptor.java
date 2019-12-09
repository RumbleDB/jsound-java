package org.jsound.api;

public class FieldDescriptor {
    public String name;
    public TypeDescriptor type;
    public String stringType;
    private Boolean required = false;
    private String defaultValue;
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

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }
}
