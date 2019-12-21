package org.jsound.atomicTypes;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.jsound.atomicItems.HexBinaryItem;
import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.item.Item;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.ItemTypes;
import org.jsound.type.TypeDescriptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.LENGTH;
import static org.jsound.facets.FacetTypes.MAX_LENGTH;
import static org.jsound.facets.FacetTypes.MIN_LENGTH;

public class HexBinaryType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Arrays.asList(LENGTH, MIN_LENGTH, MAX_LENGTH));

    public HexBinaryType(String name, AtomicFacets facets) {
        super(ItemTypes.HEXBINARY, name, facets);
    }

    public HexBinaryType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.HEXBINARY, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(Item item, boolean isEnumerationItem) {
        byte[] hexValue;
        try {
            hexValue = Hex.decodeHex(item.getStringValue().toCharArray());
        } catch (DecoderException e) {
            return false;
        }
        if (this.getFacets() == null)
            return true;
        item = new HexBinaryItem(hexValue, item.getStringValue());
        if (!validateLengthFacets(item, isEnumerationItem))
            return false;
        return recursivelyValidate(item);
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) throws DecoderException {
        byte[] hexValue = item.getBinaryValue();
        for (Item enumItem : this.getFacets().getEnumeration()) {
            if (Arrays.equals(hexValue, Hex.decodeHex(enumItem.getStringValue().toCharArray())))
                return true;
        }
        return false;
    }

    @Override
    public void checkBaseType() {
        areLengthFacetsMoreRestrictive();
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
