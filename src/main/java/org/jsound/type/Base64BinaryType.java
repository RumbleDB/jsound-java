package org.jsound.type;

import org.jsound.api.AtomicType;
import org.jsound.api.ItemTypes;

public class Base64BinaryType extends AtomicType {

    private String _defaultValue;

    Base64BinaryType(String typeString) {
        super(ItemTypes.BASE64BINARY, typeString);
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
    public boolean isBase64BinaryType() {
        return true;
    }
}
