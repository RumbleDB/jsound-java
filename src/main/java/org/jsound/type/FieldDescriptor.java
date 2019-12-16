package org.jsound.type;

import org.jsound.item.Item;

public class FieldDescriptor {
    public String name;
    private TypeOrReference type;
    private Boolean required = false;
    private Item defaultValue;
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

    public void setDefaultValue(Item defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    public String getName() {
        return name;
    }

    public TypeOrReference getTypeOrReference() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public Boolean isUnique() {
        return unique;
    }

    public Item getDefaultValue() {
        return defaultValue;
    }
}
