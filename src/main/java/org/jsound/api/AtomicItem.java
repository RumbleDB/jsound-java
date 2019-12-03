package org.jsound.api;

import org.tyson.TYSONValue;
import org.tyson.TysonItem;

public abstract class AtomicItem extends Item {

    @Override
    public boolean isValidAgainst(ItemType itemType) {
        return false;
    }

    @Override
    public TysonItem annotate(ItemType itemType) {
        return new TYSONValue(itemType.getType().getTypeName(), this.getStringAnnotation());
    }

    public abstract Object getValue();

    public abstract String getStringAnnotation();
}
