package jsound.atomicTypes;

import jsound.atomicItems.HexBinaryItem;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.types.ItemTypes;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.api.Item;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static jsound.facets.FacetTypes.LENGTH;
import static jsound.facets.FacetTypes.MAXLENGTH;
import static jsound.facets.FacetTypes.MINLENGTH;

public class HexBinaryType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Arrays.asList(LENGTH, MINLENGTH, MAXLENGTH));

    public HexBinaryType(String name, AtomicFacets facets) {
        super(ItemTypes.HEXBINARY, name, facets);
    }

    public HexBinaryType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.HEXBINARY, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(ItemWrapper itemWrapper, boolean isEnumValue) {
        byte[] hexValue;
        if (!itemWrapper.isStringItem() && !itemWrapper.isHexBinaryItem())
            return false;
        try {
            hexValue = HexBinaryItem.parseHexBinaryString(itemWrapper.getStringValue());
        } catch (IllegalArgumentException e) {
            return false;
        }
        itemWrapper.setItem(new HexBinaryItem(hexValue, itemWrapper.getStringValue()));
        return this.getFacets() == null || validateLengthFacets(itemWrapper.getItem(), isEnumValue);
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) throws DecoderException {
        byte[] hexValue = item.getBinaryValue();
        for (ItemWrapper enumItem : this.getFacets().getEnumeration()) {
            if (Arrays.equals(hexValue, Hex.decodeHex(enumItem.getStringValue().toCharArray())))
                return true;
        }
        return false;
    }

    @Override
    public void checkAgainstTypeDescriptor(TypeDescriptor typeDescriptor) {
        areLengthFacetsMoreRestrictive(typeDescriptor);
    }


    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isHexBinaryType() {
        return true;
    }

    @Override
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isHexBinaryType();
    }
}
