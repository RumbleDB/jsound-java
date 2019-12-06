package org.jsound.type;

import org.jsound.api.AtomicType;
import org.jsound.api.ItemTypes;
import org.jsound.facets.Facets;

public class Base64BinaryType extends AtomicType {

    private String _defaultValue;

    private String _name;
    private Facets _facets;

    Base64BinaryType(String name, Facets facets) {
        super(ItemTypes.BASE64BINARY);
        this._name = name;
        this._facets = facets;
    }

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
