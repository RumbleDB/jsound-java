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

public class BooleanType extends AtomicTypeDescriptor {

    public BooleanType(String name, AtomicFacets facets) {
        super(ItemTypes.BOOLEAN, name, facets);
    }

    public BooleanType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.BOOLEAN, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(ItemWrapper itemWrapper, boolean isEnumValue) {
        boolean result = itemWrapper.isBooleanItem()
            || (itemWrapper.isStringItem()
                && StringUtils.isBooleanLiteral(
                    itemWrapper.getStringValue()
                ));
        if (result && !itemWrapper.isBooleanItem())
            itemWrapper.setItem(
                ItemFactory.getInstance().createBooleanItem(Boolean.parseBoolean(itemWrapper.getStringValue()))
            );
        return result;
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
    public boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isBooleanType();
    }
}
