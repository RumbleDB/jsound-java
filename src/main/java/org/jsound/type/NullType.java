package org.jsound.type;

import org.jsound.api.AtomicType;
import org.jsound.api.ItemTypes;

public class NullType extends AtomicType {

    private String _defaultValue;

    NullType() {
        super(ItemTypes.NULL);
    }

    @Override
    protected void setDefaultValue(String typeString) {
        this._defaultValue = typeString.contains("=") ? typeString.split("=")[1] : "null";
    }

    @Override
    public boolean isNullType() {
        return true;
    }

    @Override
    public String getDefaultValueStringAnnotation() {
        return this._defaultValue;
    }
}
