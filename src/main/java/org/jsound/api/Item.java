package org.jsound.api;

import org.tyson.TysonItem;

public abstract class Item {

    public abstract boolean isValidAgainst(ItemType itemType);

    public abstract TysonItem annotate(ItemType itemType);

    public boolean isNull() {
        return false;
    }

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

    public boolean isObject() {
        return false;
    }

    public boolean isArray() {
        return false;
    }


}
