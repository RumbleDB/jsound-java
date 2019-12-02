package org.jsound.type;

import jsound.exceptions.UnexpectedTypeException;
import org.jsound.api.AtomicType;

import java.math.BigDecimal;

public class DecimalType extends AtomicType {

    private BigDecimal _defaultValue = null;

    DecimalType(String typeString) {
        super(typeString);
    }

    @Override
    protected void setDefaultValue(String typeString) {
        if (typeString.contains("=")) {
            if (typeString.contains("e") || typeString.contains("E"))
                throw new UnexpectedTypeException(typeString + " is not of type decimal.");
            _defaultValue = new BigDecimal(typeString.split("=")[1]);
        }
    }

    public BigDecimal getDefaultValue() {
        return this._defaultValue;
    }

    @Override
    public boolean isDecimalType() {
        return true;
    }
}
