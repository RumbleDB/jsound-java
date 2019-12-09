package org.jsound.item;

import org.jsound.api.AtomicItem;
import org.jsound.api.TypeDescriptor;

public class NullItem extends AtomicItem {

    NullItem() {
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
