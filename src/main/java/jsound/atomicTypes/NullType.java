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

public class NullType extends AtomicTypeDescriptor {

    public NullType(String name, AtomicFacets facets) {
        super(ItemTypes.NULL, name, facets);
    }

    public NullType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.NULL, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(Item item, boolean isEnumValue) {
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

    @Override
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isNullType();
    }
}
