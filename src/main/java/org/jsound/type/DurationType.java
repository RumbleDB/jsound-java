package org.jsound.type;

import org.jsound.api.AtomicType;
import org.jsound.api.ItemTypes;

public class DurationType extends AtomicType {

    private String _defaultValue;

    DurationType(String typeString) {
        super(ItemTypes.DURATION, typeString);
    }

    DurationType(ItemTypes type, String typeString) {
        super(type, typeString);
    }

    @Override
    protected void setDefaultValue(String typeString) {
        _defaultValue = typeString.contains("=") ? typeString.split("=")[1] : null;
    }

    @Override
    public String getDefaultValue() {
        return _defaultValue;
    }

    @Override
    public String getDefaultValueStringAnnotation() {
        return "\"" + _defaultValue + "\"";
    }

    @Override
    public boolean isDurationType() {
        return true;
    }

    public static String getPositivePeriod(String period) {
        if (period.startsWith("-"))
            return period.substring(1);
        return period;
    }
}
