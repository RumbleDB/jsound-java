package org.jsound.api;

import org.jsound.facets.FacetTypes;
import org.jsound.facets.Facets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.CLOSED;
import static org.jsound.facets.FacetTypes.CONTENT;


public class ObjectTypeDescriptor extends TypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Arrays.asList(CONTENT, CLOSED));

    public ObjectTypeDescriptor(String name, Facets facets) {
        super(ItemTypes.OBJECT, name, facets);
        this.baseType = this;
    }

    public ObjectTypeDescriptor(String name, ObjectTypeDescriptor baseType, Facets facets) {
        super(ItemTypes.OBJECT, name, baseType, facets);
    }

    @Override
    public boolean isObjectType() {
        return true;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    public void setBaseType(ObjectTypeDescriptor baseType) {
        this.baseType = baseType;
    }
}
