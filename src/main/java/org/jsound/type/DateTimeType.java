package org.jsound.type;

import org.jsound.api.AtomicType;
import org.jsound.api.ItemTypes;

public class DateTimeType extends AtomicType {

    private String _defaultValue;

    DateTimeType(String typeString) {
        super(ItemTypes.DATE, typeString);
    }

    @Override
    protected void setDefaultValue(String typeString) {
        _defaultValue = typeString.contains("=") ? typeString.split("=")[1] : null;
    }

    public String getDefaultValue() {
        return this._defaultValue;
    }

    @Override
    public String getDefaultValueStringAnnotation() {
        return "\"" + this._defaultValue + "\"";
    }

    @Override
    public boolean isDateTimeType() {
        return true;
    }
}
