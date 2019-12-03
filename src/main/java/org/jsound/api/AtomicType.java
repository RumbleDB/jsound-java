package org.jsound.api;

public abstract class AtomicType extends ItemType {

    public AtomicType(ItemTypes type) {
        super(type);
    }

    public AtomicType(ItemTypes type, String typeString) {
        super(type);
        this.setDefaultValue(typeString);
    }

    protected abstract void setDefaultValue(String typeString);
}
