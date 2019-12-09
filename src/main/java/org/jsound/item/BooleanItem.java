package org.jsound.item;

import org.jsound.api.AtomicItem;
import org.jsound.api.TypeDescriptor;


public class BooleanItem extends AtomicItem {

    private boolean _value;

    BooleanItem(boolean value) {
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
