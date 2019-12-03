package org.jsound.type;

import jsound.exceptions.UnexpectedTypeException;
import org.jsound.api.AtomicType;
import org.jsound.api.ItemTypes;
import org.jsound.utils.StringUtils;

public class BooleanType extends AtomicType {

    private boolean _defaultValue;

    BooleanType(String typeString) {
        super(ItemTypes.BOOLEAN, typeString);
    }

    @Override
    protected void setDefaultValue(String typeString) {
        if (typeString.contains("=")) {
            if (StringUtils.isBooleanLiteral(typeString)) {
                _defaultValue = Boolean.parseBoolean(typeString);
            } else
                throw new UnexpectedTypeException(typeString + " is not of type boolean.");
        }
    }

    @Override
    public Boolean getDefaultValue() {
        return this._defaultValue;
    }

    @Override
    public boolean isBooleanType() {
        return true;
    }
}
