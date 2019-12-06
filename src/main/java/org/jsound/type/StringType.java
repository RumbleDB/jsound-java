package org.jsound.type;

import org.jsound.api.AtomicType;
import org.jsound.api.ItemTypes;
import org.jsound.facets.Facets;


public class StringType extends AtomicType {

    private String _defaultValue;
    private String _name;
    private Facets _facets;

    StringType(String name, Facets facets) {
        super(ItemTypes.STRING);
        this._name = name;
        this._facets = facets;
    }

    StringType(String typeString) {
        super(ItemTypes.STRING, typeString);
    }

    @Override
    protected void setDefaultValue(String typeString) {
        _defaultValue = typeString.contains("=") ? typeString.split("=")[1] : null;
    }

    @Override
    public String getDefaultValue() {
        return this._defaultValue;
    }

    @Override
    public String getDefaultValueStringAnnotation() {
        return "\"" + this._defaultValue + "\"";
    }

    @Override
    public boolean isStringType() {
        return true;
    }
}
