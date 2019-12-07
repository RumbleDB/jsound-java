package org.jsound.type;

import org.jsound.api.AtomicTypeDescriptor;
import org.jsound.api.ItemTypes;
import org.jsound.facets.FacetTypes;
import org.jsound.facets.Facets;

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
}
