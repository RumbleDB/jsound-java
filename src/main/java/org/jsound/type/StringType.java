package org.jsound.type;

import org.jsound.api.AtomicType;
import org.jsound.api.ItemTypes;

public class StringType extends AtomicType {

    private String _defaultValue;

    StringType(String typeString) {
        super(ItemTypes.STRING, typeString);
    }

    @Override
    protected void setDefaultValue(String typeString) {
        _defaultValue = typeString.contains("=") ? typeString.split("=")[1] : null;
    }

    @Override
    public String getDefaultValue() {
        return this._defaultValue;
    }

    @Override
    public boolean isStringType() {
        return true;
    }
}
