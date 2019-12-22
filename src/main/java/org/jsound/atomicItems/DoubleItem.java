package org.jsound.atomicItems;

import org.jsound.item.AtomicItem;

public class DoubleItem extends AtomicItem {

    private Double _value;

    public DoubleItem(Double value) {
        this._value = value;
    }

    @Override
    public Double getDoubleValue() {
        return _value;
    }

    @Override
    public String getStringValue() {
        return (!this._value.isInfinite() && !this._value.isNaN()) ? this._value.toString() : "null";
    }

    @Override
    public String getStringAnnotation() {
        return this.getStringValue();
    }

    @Override
    public int hashCode() {
        return this._value.hashCode();
    }
}
