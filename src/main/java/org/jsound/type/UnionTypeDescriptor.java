package org.jsound.type;

import jsound.exceptions.InvalidSchemaException;
import org.jsound.facets.FacetTypes;
import org.jsound.facets.Facets;
import org.jsound.item.Item;
import org.tyson.TYSONValue;
import org.tyson.TysonItem;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.CONTENT;

public class UnionTypeDescriptor extends TypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Collections.singletonList(CONTENT));

    public UnionTypeDescriptor(String name, Facets facets) {
        super(ItemTypes.VALUE, name, facets);
        this.baseType = new TypeOrReference(this);
    }

    public UnionTypeDescriptor(String name, TypeOrReference baseType, Facets facets) {
        super(ItemTypes.VALUE, name, baseType, facets);
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isUnionType() {
        return true;
    }

    public boolean validate(Item item) {
        TypeDescriptor typeDescriptor;
        for (TypeOrReference typeOrReference : this.getFacets().unionContent.getTypes()) {
            typeDescriptor = typeOrReference.getTypeDescriptor();
            if (item.isValidAgainst(typeDescriptor))
                return true;
        }
        return false;
    }

    public TysonItem annotate(Item item) {
        TypeDescriptor typeDescriptor;
        for (TypeOrReference typeOrReference : this.getFacets().unionContent.getTypes()) {
            typeDescriptor = typeOrReference.getTypeDescriptor();
            if (item.isValidAgainst(typeDescriptor))
                return new TYSONValue(typeDescriptor.getName(), item.getStringAnnotation());
        }
        throw new InvalidSchemaException(
                item.getStringAnnotation() + " cannot is not valid against any type of union " + this.getName()
        );
    }
}
