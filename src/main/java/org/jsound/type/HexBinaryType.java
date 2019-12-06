package org.jsound.type;

import org.jsound.api.AtomicType;
import org.jsound.api.ItemTypes;
import org.jsound.facets.Facets;

public class HexBinaryType extends AtomicType {

    private String _defaultValue;

    private String _name;
    private Facets _facets;

    HexBinaryType(String name, Facets facets) {
        super(ItemTypes.HEXBINARY);
        this._name = name;
        this._facets = facets;
    }

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
