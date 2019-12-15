package org.jsound.atomicTypes;

import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.item.Item;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.ItemTypes;
import org.jsound.utils.StringUtils;

import java.util.Collections;
import java.util.Set;

public class NullType extends AtomicTypeDescriptor {

    public NullType(String name, AtomicFacets facets) {
        super(ItemTypes.NULL, name, facets);
    }

    public NullType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.NULL, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(Item item) {
        return item.isNull() || (item.isString() && StringUtils.isNullLiteral(item.getStringValue()));
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
