package org.jsound.item;

import org.jsound.api.AtomicItem;
import org.jsound.api.TypeDescriptor;

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
    public boolean isValidAgainst(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isIntegerType()
            || typeDescriptor.isDecimalType()
            || typeDescriptor.isDoubleType()
            || super.isValidAgainst(typeDescriptor);
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
