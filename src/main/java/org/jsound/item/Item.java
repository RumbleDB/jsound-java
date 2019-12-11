package org.jsound.item;

import org.jsound.type.TypeDescriptor;
import org.tyson.TysonItem;

public abstract class Item {

    public abstract boolean isValidAgainst(TypeDescriptor typeDescriptor);

    public abstract TysonItem annotateWith(TypeDescriptor typeDescriptor);

    public boolean isString() {
        return false;
    }

    public boolean isInteger() {
        return false;
    }

    public boolean isDecimal() {
        return false;
    }

    public boolean isDouble() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isNull() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isArray() {
        return false;
    }

    public abstract String getStringAnnotation();
}
