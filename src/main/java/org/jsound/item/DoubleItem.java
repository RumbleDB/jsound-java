package org.jsound.item;

import org.jsound.api.AtomicItem;
import org.jsound.api.ItemType;

public class DoubleItem extends AtomicItem {

    private Double _value;

    DoubleItem(Double value) {
        this._value = value;
    }

    public Double getValue() {
        return this._value;
    }

    @Override
    public boolean isValidAgainst(ItemType itemType) {
        return itemType.isDoubleType() || super.isValidAgainst(itemType);
    }

    @Override public int hashCode() {
        return this._value.hashCode();
    }
}
