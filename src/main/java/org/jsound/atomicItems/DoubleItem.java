package org.jsound.atomicItems;

import org.jsound.item.AtomicItem;
import org.jsound.type.TypeDescriptor;

public class DoubleItem extends AtomicItem {

    private Double _value;

    public DoubleItem(Double value) {
        this._value = value;
    }

    @Override
    public Double getValue() {
        return this._value;
    }

    @Override
    public boolean isValidAgainst(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isDoubleType() || super.isValidAgainst(typeDescriptor);
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
