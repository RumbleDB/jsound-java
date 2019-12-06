package org.jsound.type;

import jsound.exceptions.UnexpectedTypeException;
import org.jsound.api.AtomicType;
import org.jsound.api.ItemTypes;
import org.jsound.facets.Facets;

import java.math.BigDecimal;

public class DecimalType extends AtomicType {

    private BigDecimal _defaultValue = null;

    private String _name;
    private Facets _facets;

    DecimalType(String typeString) {
        super(ItemTypes.DECIMAL, typeString);
    }

    DecimalType(String name, Facets facets) {
        super(ItemTypes.DECIMAL);
        this._name = name;
        this._facets = facets;
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
    public String getDefaultValueStringAnnotation() {
        return this._defaultValue.toString();
    }

    @Override
    public boolean isDecimalType() {
        return true;
    }
}
