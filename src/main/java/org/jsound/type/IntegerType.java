package org.jsound.type;

import org.jsound.api.AtomicType;
import org.jsound.api.ItemTypes;
import org.jsound.facets.Facets;

public class IntegerType extends AtomicType {

    private Integer _defaultValue;

    private String _name;
    private Facets _facets;

    IntegerType(String typeString) {
        super(ItemTypes.INTEGER, typeString);
    }

    IntegerType(String name, Facets facets) {
        super(ItemTypes.INTEGER);
        this._name = name;
        this._facets = facets;
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
