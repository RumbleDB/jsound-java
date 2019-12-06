package org.jsound.type;

import org.jsound.api.AtomicType;
import org.jsound.api.ItemTypes;
import org.jsound.facets.Facets;

public class DoubleType extends AtomicType {

    private Double _defaultValue;

    private String _name;
    private Facets _facets;

    DoubleType(String typeString) {
        super(ItemTypes.DOUBLE, typeString);
    }

    DoubleType(String name, Facets facets) {
        super(ItemTypes.DOUBLE);
        this._name = name;
        this._facets = facets;
    }

    @Override
    protected void setDefaultValue(String typeString) {
        _defaultValue = typeString.contains("=") ? Double.parseDouble(typeString.split("=")[1]) : null;
    }

    @Override
    public Double getDefaultValue() {
        return this._defaultValue;
    }

    @Override
    public String getDefaultValueStringAnnotation() {
        return (!this._defaultValue.isInfinite() && !this._defaultValue.isNaN())
            ? this._defaultValue.toString()
            : "null";
    }

    @Override
    public boolean isDoubleType() {
        return true;
    }
}
