package org.jsound.item;

import org.jsound.api.AtomicItem;
import org.jsound.api.ItemType;
import org.jsound.utils.StringUtils;


public class StringItem extends AtomicItem {

    private String _value;

    StringItem(String string) {
        this._value = string;
    }

    @Override
    public String getValue() {
        return this._value;
    }

    @Override
    public String getAnnotationString() {
        return "\"" + this._value + "\"";
    }

    @Override
    public boolean isValidAgainst(ItemType itemType) {
        if (itemType.isStringType() || super.isValidAgainst(itemType))
            return true;
        try {
            if (itemType.isIntegerType()) {
                Integer.parseInt(this._value);
            } else if (itemType.isDecimalType()) {
                if (this._value.contains("e") || this._value.contains("E"))
                    return false;
                Float.parseFloat(this._value);
            } else if (itemType.isDoubleType()) {
                Double.parseDouble(this._value);
            } else if (itemType.isNullType()) {
                return StringUtils.isNullLiteral(this._value);
            } else if (itemType.isBooleanType()) {
                return StringUtils.isBooleanLiteral(this._value);
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this._value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StringItem))
            return false;
        return this._value.equals(((StringItem) o)._value);
    }
}
