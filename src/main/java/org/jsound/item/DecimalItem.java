package org.jsound.item;

import org.jsound.api.AtomicItem;
import org.jsound.api.ItemType;

import java.math.BigDecimal;

public class DecimalItem extends AtomicItem {

    private BigDecimal _value;

    DecimalItem(BigDecimal value) {
        this._value = value;
    }

    public BigDecimal getValue() {
        return this._value;
    }

    @Override
    public boolean isValidAgainst(ItemType itemType) {
        return itemType.isDecimalType() || itemType.isDoubleType() || super.isValidAgainst(itemType);
    }

    @Override public int hashCode() {
        return this._value.hashCode();
    }
}
