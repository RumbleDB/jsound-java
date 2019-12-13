package org.jsound.type;

import jsound.exceptions.InvalidSchemaException;
import org.jsound.facets.FacetTypes;
import org.jsound.facets.ObjectFacets;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.cli.JSoundExecutor.object;
import static org.jsound.facets.FacetTypes.CLOSED;
import static org.jsound.facets.FacetTypes.CONTENT;
import static org.jsound.json.SchemaFileJsonParser.commonFacets;


public class ObjectTypeDescriptor extends TypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Arrays.asList(CONTENT, CLOSED));
    private final ObjectFacets facets;

    public ObjectTypeDescriptor(String name, ObjectFacets facets) {
        super(ItemTypes.OBJECT, name);
        this.baseType = new TypeOrReference(this);
        this.facets = facets;
    }

    public ObjectTypeDescriptor(String name, TypeOrReference baseType, ObjectFacets facets) {
        super(ItemTypes.OBJECT, name, baseType);
        this.facets = facets;
    }

    @Override
    public boolean isObjectType() {
        return true;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public ObjectFacets getFacets() {
        return facets;
    }

    public static ObjectFacets createFacets(ObjectFacets facets) throws IOException {
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

    public static ObjectFacets createFacets() throws IOException {
        return createFacets(new ObjectFacets());
    }
}
