package org.jsound.type;

import jsound.exceptions.InvalidSchemaException;
import org.jsound.facets.FacetTypes;
import org.jsound.facets.UnionFacets;
import org.jsound.item.Item;
import org.tyson.TYSONValue;
import org.tyson.TysonItem;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.CONTENT;

public class UnionTypeDescriptor extends TypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Collections.singletonList(CONTENT));
    private final UnionFacets facets;

    public UnionTypeDescriptor(String name, UnionFacets facets) {
        super(ItemTypes.VALUE, name);
        this.baseType = new TypeOrReference(this);
        this.facets = facets;
    }

    public UnionTypeDescriptor(String name, TypeOrReference baseType, UnionFacets facets) {
        super(ItemTypes.VALUE, name, baseType);
        this.facets = facets;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isUnionType() {
        return true;
    }

    @Override
    public boolean validate(Item item) {
        TypeDescriptor typeDescriptor;
        for (TypeOrReference typeOrReference : this.getFacets().getUnionContent().getTypes()) {
            typeDescriptor = typeOrReference.getTypeDescriptor();
            if (typeDescriptor.validate(item))
                return true;
        }
        return false;
    }

    @Override
    public TysonItem annotate(Item item) {
        TypeDescriptor typeDescriptor;
        for (TypeOrReference typeOrReference : this.getFacets().getUnionContent().getTypes()) {
            typeDescriptor = typeOrReference.getTypeDescriptor();
            if (typeDescriptor.validate(item))
                return new TYSONValue(typeDescriptor.getName(), item);
        }
        throw new InvalidSchemaException(
                item.getStringValue() + " cannot is not valid against any type of union " + this.getName()
        );
    }

    @Override
    public UnionFacets getFacets() {
        return facets;
    }
}
