package org.jsound.api;

public abstract class AtomicItem extends Item {

    @Override
    public boolean isValidAgainst(ItemType itemType) {
        return false;
    }

    @Override
    public Object annotate(ItemType itemType) {
        return " (" + itemType.getType().getTypeName() + ") " + this.getValue();
    }

    protected abstract Object getValue();
}
