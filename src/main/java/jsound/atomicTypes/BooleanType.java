package jsound.atomicTypes;

import org.api.TypeDescriptor;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import org.api.Item;
import jsound.types.ItemTypes;
import jsound.utils.StringUtils;

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
    public boolean validate(Item item, boolean isEnumValue) {
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
