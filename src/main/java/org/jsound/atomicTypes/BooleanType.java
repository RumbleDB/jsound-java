package org.jsound.atomicTypes;

import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.item.Item;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.ItemTypes;
import org.jsound.type.TypeDescriptor;
import org.jsound.utils.StringUtils;

import java.util.Collections;
import java.util.Set;

public class BooleanType extends AtomicTypeDescriptor {

    public BooleanType(String name, AtomicFacets facets) {
        super(ItemTypes.BOOLEAN, name, facets);
    }

    public BooleanType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.BOOLEAN, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(Item item, boolean isEnumerationItem) {
        return item.isBoolean() || (item.isString() && StringUtils.isBooleanLiteral(item.getStringValue()));
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return Collections.emptySet();
    }

    @Override
    public boolean isBooleanType() {
        return true;
    }

    @Override
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isBooleanType();
    }
}
