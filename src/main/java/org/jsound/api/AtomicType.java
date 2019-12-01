package org.jsound.api;

public abstract class AtomicType extends ItemType {

    public AtomicType(String typeString) {
        this.setDefaultValue(typeString);
    }

    protected abstract void setDefaultValue(String typeString);
}
