package org.jsound.item;

import org.jsound.api.AtomicItem;
import org.jsound.api.ItemType;

public class IntegerItem extends AtomicItem {

    private Integer _value;

    IntegerItem(Integer integer) {
        this._value = integer;
    }

    @Override
    public Integer getValue() {
        return this._value;
    }

    @Override
    public boolean isValidAgainst(ItemType itemType) {
        return itemType.isIntegerType()
            || itemType.isDecimalType()
            || itemType.isDoubleType()
            || super.isValidAgainst(itemType);
    }

    @Override
    public String getStringAnnotation() {
        return this._value.toString();
    }

    @Override
    public int hashCode() {
        return this._value.hashCode();
    }
}
