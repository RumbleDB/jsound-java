package jsound.atomicTypes;

import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import jsound.item.ItemFactory;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.types.ItemTypes;
import jsound.utils.StringUtils;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;

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
    public boolean validate(ItemWrapper itemWrapper, boolean isEnumValue) {
        boolean result = itemWrapper.isNullItem()
            || (itemWrapper.isStringItem()
                && StringUtils.isNullLiteral(
                    itemWrapper.getStringValue()
                ));
        if (result && !itemWrapper.isNullItem())
            itemWrapper.setItem(ItemFactory.getInstance().createNullItem());
        return result;
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
    public boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isNullType();
    }
}
