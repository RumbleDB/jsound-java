package org.jsound.item;

import org.jsound.api.AtomicItem;
import org.jsound.api.ItemType;

public class DoubleItem extends AtomicItem {

    private Double _value;

    DoubleItem(Double value) {
        this._value = value;
    }

    @Override
    public Double getValue() {
        return this._value;
    }

    @Override
    public boolean isValidAgainst(ItemType itemType) {
        return itemType.isDoubleType() || super.isValidAgainst(itemType);
    }

    @Override
    public String getStringAnnotation() {
        return (!this._value.isInfinite() && !this._value.isNaN()) ? this._value.toString() : "null";
    }

    @Override
    public int hashCode() {
        return this._value.hashCode();
    }
}
