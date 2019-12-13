package org.jsound.atomicTypes;

import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.ItemTypes;

import java.util.Collections;
import java.util.Set;

public class NullType extends AtomicTypeDescriptor {

    public NullType(String name, AtomicFacets facets) {
        super(ItemTypes.NULL, name, facets);
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return Collections.emptySet();
    }

    @Override
    public boolean isNullType() {
        return true;
    }
}
