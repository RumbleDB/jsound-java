package jsound.atomicTypes;

import jsound.atomicItems.Base64BinaryItem;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.types.ItemTypes;
import org.apache.commons.codec.binary.Base64;
import org.api.Item;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static jsound.facets.FacetTypes.LENGTH;
import static jsound.facets.FacetTypes.MAXLENGTH;
import static jsound.facets.FacetTypes.MINLENGTH;

public class Base64BinaryType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Arrays.asList(LENGTH, MINLENGTH, MAXLENGTH));

    public Base64BinaryType(String name, AtomicFacets facets) {
        super(ItemTypes.BASE64BINARY, name, facets);
    }

    public Base64BinaryType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.BASE64BINARY, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(ItemWrapper itemWrapper, boolean isEnumValue) {
        byte[] base64BinaryValue;
        if (!itemWrapper.isStringItem() && !itemWrapper.isBase64BinaryItem())
            return false;
        try {
            base64BinaryValue = Base64BinaryItem.parseBase64BinaryString(itemWrapper.getStringValue());
        } catch (IllegalArgumentException e) {
            return false;
        }
        itemWrapper.setItem(new Base64BinaryItem(base64BinaryValue, itemWrapper.getStringValue()));
        return this.getFacets() == null || validateLengthFacets(itemWrapper.getItem(), isEnumValue);
    }

    @Override
    public void checkAgainstTypeDescriptor(TypeDescriptor typeDescriptor) {
        areLengthFacetsMoreRestrictive(typeDescriptor);
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) {
        byte[] base64 = item.getBinaryValue();
        for (ItemWrapper enumItem : this.getFacets().getEnumeration()) {
            if (Arrays.equals(base64, Base64.decodeBase64(enumItem.getStringValue())))
                return true;
        }
        return false;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isBase64BinaryType() {
        return true;
    }

    @Override
    public boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isBase64BinaryType();
    }
}
