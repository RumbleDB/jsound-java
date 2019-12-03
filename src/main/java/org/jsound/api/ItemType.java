package org.jsound.api;

public class ItemType {

    private ItemTypes type;

    public ItemType() {
    }

    public ItemType(ItemTypes type) {
        this.type = type;
    }

    public boolean isStringType() {
        return false;
    }

    public boolean isIntegerType() {
        return false;
    }

    public boolean isDecimalType() {
        return false;
    }

    public boolean isDoubleType() {
        return false;
    }

    public boolean isBooleanType() {
        return false;
    }

    public boolean isObjectType() {
        return false;
    }

    public boolean isArrayType() {
        return false;
    }

    public boolean isNullType() {
        return false;
    }

    public boolean isUserDefinedType() {
        return false;
    }

    public Object getDefaultValue() {
        return null;
    }

    public ItemTypes getType() {
        return this.type;
    }
}
