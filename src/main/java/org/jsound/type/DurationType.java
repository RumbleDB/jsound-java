package org.jsound.type;

import org.jsound.api.AtomicTypeDescriptor;
import org.jsound.api.ItemTypes;
import org.jsound.facets.FacetTypes;
import org.jsound.facets.Facets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.MAX_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MAX_INCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_INCLUSIVE;

public class DurationType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(
            Arrays.asList(MIN_INCLUSIVE, MAX_INCLUSIVE, MIN_EXCLUSIVE, MAX_EXCLUSIVE)
    );

    public DurationType(String name, Facets facets) {
        super(ItemTypes.DURATION, name, facets);
        this.baseType = this;
    }

    DurationType(ItemTypes durationSubtype, String name, Facets facets) {
        super(durationSubtype, name, facets);
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }
}
