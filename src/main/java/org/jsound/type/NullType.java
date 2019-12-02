package org.jsound.type;

import org.jsound.api.AtomicType;

public class NullType extends AtomicType {

    NullType() {
        super();
    }

    @Override
    protected void setDefaultValue(String typeString) {
    }

    @Override
    public boolean isNullType() {
        return true;
    }
}
