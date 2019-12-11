package org.jsound.atomicTypes;

import org.jsound.facets.FacetTypes;
import org.jsound.facets.Facets;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.ItemTypes;

import java.util.Collections;
import java.util.Set;

public class BooleanType extends AtomicTypeDescriptor {

    public BooleanType(String name, Facets facets) {
        super(ItemTypes.BOOLEAN, name, facets);
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return Collections.emptySet();
    }

    @Override
    public boolean isBooleanType() {
        return true;
    }
}
