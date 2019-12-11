package org.jsound.atomicItems;

import org.jsound.item.AtomicItem;
import org.jsound.type.TypeDescriptor;


public class BooleanItem extends AtomicItem {

    private boolean _value;

    public BooleanItem(boolean value) {
        this._value = value;
    }

    @Override
    public Boolean getValue() {
        return this._value;
    }

    @Override
    public boolean isValidAgainst(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isBooleanType() || super.isValidAgainst(typeDescriptor);
    }

    @Override
    public String getStringAnnotation() {
        return Boolean.toString(this._value);
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(this._value);
    }
}
