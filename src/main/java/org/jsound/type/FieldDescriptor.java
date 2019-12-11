package org.jsound.type;

public class FieldDescriptor {
    public String name;
    private TypeOrReference type;
    private Boolean required = false;
    private String defaultValue;
    private Boolean unique = false;

    public void setName(String name) {
        this.name = name;
    }

    public void setType(TypeOrReference type) {
        this.type = type;
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

    public String getName() {
        return name;
    }

    public TypeOrReference getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public Boolean isUnique() {
        return unique;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getDefaultValueAnnotation() {
        return "\"" + this.defaultValue + "\"";
    }
}
