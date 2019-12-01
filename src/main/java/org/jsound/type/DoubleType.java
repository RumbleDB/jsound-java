package org.jsound.type;

import org.jsound.api.AtomicType;

public class DoubleType extends AtomicType {

    private Double _defaultValue;

    DoubleType(String typeString) {
        super(typeString);
    }

    @Override protected void setDefaultValue(String typeString) {
        _defaultValue = typeString.contains("=") ? Double.parseDouble(typeString.split("=")[1]) : null;
    }

    public Double getDefaultValue() {
        return this._defaultValue;
    }
}
