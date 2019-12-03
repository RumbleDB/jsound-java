package org.jsound.type;

import org.jsound.api.AtomicType;
import org.jsound.api.ItemTypes;

public class NullType extends AtomicType {

    NullType() {
        super(ItemTypes.NULL);
    }

    @Override
    protected void setDefaultValue(String typeString) {
    }

    @Override
    public boolean isNullType() {
        return true;
    }
}
