package org.jsound.type;

import org.jsound.api.AtomicType;
import org.jsound.api.ItemTypes;

public class IntegerType extends AtomicType {

    private Integer _defaultValue;

    IntegerType(String typeString) {
        super(ItemTypes.INTEGER, typeString);
    }

    @Override
    protected void setDefaultValue(String typeString) {
        _defaultValue = typeString.contains("=") ? Integer.parseInt(typeString.split("=")[1]) : null;
    }

    @Override
    public Integer getDefaultValue() {
        return this._defaultValue;
    }

    @Override
    public String getDefaultValueStringAnnotation() {
        return this._defaultValue.toString();
    }

    @Override
    public boolean isIntegerType() {
        return true;
    }
}
