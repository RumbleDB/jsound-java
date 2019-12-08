package org.jsound.api;

import org.jsound.facets.FacetTypes;
import org.jsound.facets.Facets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.CONTENT;
import static org.jsound.facets.FacetTypes.MAX_LENGTH;
import static org.jsound.facets.FacetTypes.MIN_LENGTH;

public class ArrayTypeDescriptor extends TypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Arrays.asList(CONTENT, MIN_LENGTH, MAX_LENGTH));

    public ArrayTypeDescriptor(String name, Facets facets) {
        super(ItemTypes.ARRAY, name, facets);
        this.baseType = this;
    }

    public ArrayTypeDescriptor(String name, ArrayTypeDescriptor baseType, Facets facets) {
        super(ItemTypes.ARRAY, name, baseType, facets);
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isArrayType() {
        return true;
    }
}
