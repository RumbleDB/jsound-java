package org.jsound.type;

import org.jsound.api.AtomicType;

public class StringType extends AtomicType {

    private String _defaultValue;

    StringType(String typeString) {
        super(typeString);
    }

    @Override
    protected void setDefaultValue(String typeString) {
        _defaultValue = typeString.contains("=") ? typeString.split("=")[1] : null;
    }

    public String getDefaultValue() {
        return this._defaultValue;
    }
}
