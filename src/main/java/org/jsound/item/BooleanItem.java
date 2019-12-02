package org.jsound.item;

import org.jsound.api.AtomicItem;
import org.jsound.api.ItemType;

public class BooleanItem extends AtomicItem {

    private boolean _value;

    BooleanItem(boolean value) {
        this._value = value;
    }

    public boolean getValue() {
        return this._value;
    }

    @Override
    public boolean isValidAgainst(ItemType itemType) {
        return itemType.isBooleanType() || super.isValidAgainst(itemType);
    }

    @Override public int hashCode() {
        return Boolean.hashCode(this._value);
    }
}
