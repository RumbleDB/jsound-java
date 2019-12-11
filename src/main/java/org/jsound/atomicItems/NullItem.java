package org.jsound.atomicItems;

import org.jsound.item.AtomicItem;
import org.jsound.type.TypeDescriptor;

public class NullItem extends AtomicItem {

    public NullItem() {
    }

    public boolean isNull() {
        return true;
    }

    @Override
    public boolean isValidAgainst(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isNullType() || super.isValidAgainst(typeDescriptor);
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public String getStringAnnotation() {
        return "null";
    }
}
