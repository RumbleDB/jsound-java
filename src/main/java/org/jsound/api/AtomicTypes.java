package org.jsound.api;

import org.jsound.facets.FacetTypes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.*;

public enum AtomicTypes {
    STRING(new HashSet<>(Arrays.asList(LENGTH, MIN_LENGTH, MAX_LENGTH))),
    INTEGER(
            new HashSet<>(
                    Arrays.asList(
                        MIN_INCLUSIVE,
                        MAX_INCLUSIVE,
                        MIN_EXCLUSIVE,
                        MAX_EXCLUSIVE,
                        TOTAL_DIGITS,
                        FRACTION_DIGITS
                    )
            )
    ),
    DECIMAL(
            new HashSet<>(
                    Arrays.asList(
                        MIN_INCLUSIVE,
                        MAX_INCLUSIVE,
                        MIN_EXCLUSIVE,
                        MAX_EXCLUSIVE,
                        TOTAL_DIGITS,
                        FRACTION_DIGITS
                    )
            )
    ),
    DOUBLE(new HashSet<>(Arrays.asList(MIN_INCLUSIVE, MAX_INCLUSIVE, MIN_EXCLUSIVE, MAX_EXCLUSIVE))),
    HEXBINARY(new HashSet<>(Arrays.asList(LENGTH, MIN_LENGTH, MAX_LENGTH))),
    BASE64BINARY(new HashSet<>(Arrays.asList(LENGTH, MIN_LENGTH, MAX_LENGTH))),
    ANYURI(new HashSet<>(Arrays.asList(LENGTH, MIN_LENGTH, MAX_LENGTH)));

    private final Set<FacetTypes> _allowedFacets;

    AtomicTypes(Set<FacetTypes> allowedFacets) {
        this._allowedFacets = allowedFacets;
    }

    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }
}
