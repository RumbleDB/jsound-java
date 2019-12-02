package org.jsound.api;

public abstract class Item {

    public boolean isValidAgainst(ItemType itemType) {
        return true;
    }

    public boolean isNull() {
        return false;
    }

    public boolean isObject() {
        return false;
    }
}
