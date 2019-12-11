package org.jsound.atomicItems;

import org.jsound.item.AtomicItem;
import org.jsound.type.TypeDescriptor;

import java.math.BigDecimal;

public class DecimalItem extends AtomicItem {

    private BigDecimal _value;

    public DecimalItem(BigDecimal value) {
        this._value = value;
    }

    @Override
    public BigDecimal getValue() {
        return this._value;
    }

    @Override
    public boolean isValidAgainst(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isDecimalType() || typeDescriptor.isDoubleType() || super.isValidAgainst(typeDescriptor);
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
