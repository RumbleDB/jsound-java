package org.jsound.type;

import jsound.exceptions.InvalidSchemaException;
import org.jsound.facets.ArrayFacets;
import org.jsound.facets.FacetTypes;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.cli.JSoundExecutor.object;
import static org.jsound.facets.FacetTypes.CONTENT;
import static org.jsound.facets.FacetTypes.MAX_LENGTH;
import static org.jsound.facets.FacetTypes.MIN_LENGTH;
import static org.jsound.json.SchemaFileJsonParser.commonFacets;

public class ArrayTypeDescriptor extends TypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Arrays.asList(CONTENT, MIN_LENGTH, MAX_LENGTH));
    private final ArrayFacets facets;

    public ArrayTypeDescriptor(String name, ArrayFacets facets) {
        super(ItemTypes.ARRAY, name);
        this.baseType = new TypeOrReference(this);
        this.facets = facets;
    }

    public ArrayTypeDescriptor(String name, TypeOrReference baseType, ArrayFacets facets) {
        super(ItemTypes.ARRAY, name, baseType);
        this.facets = facets;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isArrayType() {
        return true;
    }

    @Override
    public ArrayFacets getFacets() {
        return facets;
    }

    public static ArrayFacets createFacets(ArrayFacets facets) throws IOException {
        String key;
        while ((key = object.readObject()) != null) {
            try {
                FacetTypes facetTypes = FacetTypes.valueOf(key);
                if (!(_allowedFacets.contains(facetTypes) || commonFacets.contains(facetTypes)))
                    throw new InvalidSchemaException("Invalid facet " + key + ".");
                facets.setFacet(facetTypes);
            } catch (IllegalArgumentException e) {
                throw new InvalidSchemaException("Invalid facet " + key + ".");
            }
        }
        return facets;
    }

    public static ArrayFacets createFacets() throws IOException {
        return createFacets(new ArrayFacets());
    }
}
