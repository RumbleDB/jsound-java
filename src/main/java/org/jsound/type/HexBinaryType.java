package org.jsound.type;

import org.jsound.api.AtomicType;
import org.jsound.api.ItemTypes;

public class HexBinaryType extends AtomicType {

    private String _defaultValue;

    HexBinaryType(String typeString) {
        super(ItemTypes.HEXBINARY, typeString);
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
    public boolean isHexBinaryType() {
        return true;
    }
}
