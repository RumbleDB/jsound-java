package org.jsound.api;

public abstract class ItemType {

    protected ItemTypes type;

    public ItemType() {
    }

    public ItemType(ItemTypes type) {
        this.type = type;
    }

    public boolean isAtomicType() {
        return false;
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

    public boolean isDateTimeType() {
        return false;
    }

    public boolean isDateType() {
        return false;
    }

    public boolean isTimeType() {
        return false;
    }

    public boolean isDurationType() {
        return false;
    }

    public boolean isYearMonthDurationType() {
        return false;
    }

    public boolean isDayTimeDurationType() {
        return false;
    }

    public boolean isHexBinaryType() {
        return false;
    }

    public boolean isBase64BinaryType() {
        return false;
    }

    public boolean isNullType() {
        return false;
    }

    public Object getDefaultValue() {
        return null;
    }

    public String getDefaultValueStringAnnotation() {
        return null;
    }

    public ItemType getItemType() {
        return this;
    }

    public String getTypeName() {
        return this.type.getTypeName();
    }
}
