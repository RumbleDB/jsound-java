package org.jsound.api;

import org.tyson.TYSONValue;
import org.tyson.TysonItem;

public abstract class AtomicItem extends Item {

    @Override
    public boolean isValidAgainst(TypeDescriptor typeDescriptor) {
        if (typeDescriptor.isUnionType())
            return ((UnionTypeDescriptor) typeDescriptor).validate(this);
        return false;
    }

    @Override
    public TysonItem annotateWith(TypeDescriptor typeDescriptor) {
        if (typeDescriptor.isUnionType())
            return ((UnionTypeDescriptor) typeDescriptor).annotate(this);
        return new TYSONValue(typeDescriptor.getName(), this.getStringAnnotation());
    }

    public abstract Object getValue();
}
