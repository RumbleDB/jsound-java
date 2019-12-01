package org.jsound.type;

import org.jsound.api.AtomicType;

public class IntegerType extends AtomicType {

    private Integer _defaultValue;

    IntegerType(String typeString) {
        super(typeString);
    }

    @Override
    protected void setDefaultValue(String typeString) {
        _defaultValue = typeString.contains("=") ? Integer.parseInt(typeString.split("=")[1]) : null;
    }

    public Integer getDefaultValue() {
        return this._defaultValue;
    }
}
