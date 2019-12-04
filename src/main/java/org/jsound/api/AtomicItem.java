package org.jsound.api;

import org.jsound.type.UnionType;
import org.tyson.TYSONValue;
import org.tyson.TysonItem;

public abstract class AtomicItem extends Item {

    @Override
    public boolean isValidAgainst(ItemType itemType) {
        if (itemType.isUnionType())
            return ((UnionType) itemType).validate(this);
        return false;
    }

    @Override
    public TysonItem annotateWith(ItemType itemType) {
        if (itemType.isUnionType())
            return ((UnionType) itemType).annotate(this);
        return new TYSONValue(itemType.getTypeName(), this.getStringAnnotation());
    }

    public abstract Object getValue();
}
