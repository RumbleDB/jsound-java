package org.jsound.atomicTypes;

import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.ItemTypes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.FRACTION_DIGITS;
import static org.jsound.facets.FacetTypes.MAX_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MAX_INCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_INCLUSIVE;
import static org.jsound.facets.FacetTypes.TOTAL_DIGITS;

public class IntegerType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(
            Arrays.asList(
                MIN_INCLUSIVE,
                MAX_INCLUSIVE,
                MIN_EXCLUSIVE,
                MAX_EXCLUSIVE,
                TOTAL_DIGITS,
                FRACTION_DIGITS
            )
    );

    public IntegerType(String name, AtomicFacets facets) {
        super(ItemTypes.INTEGER, name, facets);
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isIntegerType() {
        return true;
    }
}
