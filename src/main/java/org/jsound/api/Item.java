package org.jsound.api;

public abstract class Item {

    public abstract boolean isValidAgainst(ItemType itemType);

    public abstract Object annotate(ItemType itemType);

    public boolean isNull() {
        return false;
    }

    public boolean isObject() {
        return false;
    }
}
