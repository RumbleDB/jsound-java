package org.jsound.type;

import org.jsound.api.AtomicType;
import org.jsound.api.ItemTypes;
import org.jsound.facets.Facets;

public class AnyURIType extends AtomicType {

    private String _name;
    private Facets _facets;

    AnyURIType(String typeString) {
        super(ItemTypes.ANYURI, typeString);
    }

    AnyURIType(String name, Facets facets) {
        super(ItemTypes.ANYURI);
        this._name = name;
        this._facets = facets;
    }

    @Override
    protected void setDefaultValue(String typeString) {
    }

}
