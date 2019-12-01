package org.jsound.type;

import jsound.exceptions.UnexpectedTypeException;
import org.jsound.api.AtomicType;

public class BooleanType extends AtomicType {

    private boolean _defaultValue;

    BooleanType(String typeString) {
        super(typeString);
    }

    @Override
    protected void setDefaultValue(String typeString) {
        if (typeString.contains("=")) {
            if (isBooleanLiteral(typeString)) {
                _defaultValue = Boolean.parseBoolean(typeString);
            }
            else
                throw new UnexpectedTypeException(typeString + " is not of type boolean.");
        }
    }

    private boolean isBooleanLiteral(String value) {
        return "true".equals(value) || "false".equals(value);
    }

    public boolean getDefaultValue() {
        return this._defaultValue;
    }
}
