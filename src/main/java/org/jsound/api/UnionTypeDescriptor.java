package org.jsound.api;

import org.jsound.facets.FacetTypes;
import org.jsound.facets.Facets;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.CONTENT;

public class UnionTypeDescriptor extends TypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Collections.singletonList(CONTENT));

    public UnionTypeDescriptor(String name, Facets facets) {
        super(ItemTypes.UNION, name, facets);
        this.baseType = this;
    }

    public UnionTypeDescriptor(String name, UnionTypeDescriptor baseType, Facets facets) {
        super(ItemTypes.UNION, name, baseType, facets);
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isUnionType() {
        return true;
    }
}
