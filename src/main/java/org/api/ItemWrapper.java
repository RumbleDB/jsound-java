package org.api;

public class ItemWrapper {
    private Item item;

    public ItemWrapper(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public boolean isAnyURIItem() {
        return this.item.isAnyURIItem();
    }

    public boolean isStringItem() {
        return this.item.isStringItem();
    }

    public boolean isBooleanItem() {
        return this.item.isBooleanItem();
    }

    public boolean isIntegerItem() {
        return item.isIntegerItem();
    }

    public boolean isDecimalItem() {
        return item.isDecimalItem();
    }

    public boolean isDoubleItem() {
        return item.isDoubleItem();
    }

    public boolean isDateTimeItem() {
        return this.item.isDateTimeItem();
    }

    public boolean isDateItem() {
        return this.item.isDateItem();
    }

    public boolean isTimeItem() {
        return this.item.isTimeItem();
    }

    public boolean isDurationItem() {
        return this.item.isDurationItem();
    }

    public boolean isDayTimeDurationItem() {
        return item.isDayTimeDurationItem();
    }

    public boolean isBase64BinaryItem() {
        return this.item.isBase64BinaryItem();
    }

    public boolean isHexBinaryItem() {
        return this.item.isHexBinaryItem();
    }

    public boolean isNullItem() {
        return this.item.isNullItem();
    }

    public boolean isObjectItem() {
        return this.item.isObjectItem();
    }

    public boolean isArrayItem() {
        return this.item.isArrayItem();
    }

    public String getStringValue() {
        return this.item.getStringValue();
    }

    public String getStringAnnotation() {
        return this.item.getStringAnnotation();
    }

    public boolean isYearMonthDurationItem() {
        return item.isYearMonthDurationItem();
    }
}
