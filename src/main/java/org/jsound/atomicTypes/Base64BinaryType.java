package org.jsound.atomicTypes;

import jsound.exceptions.LessRestrictiveFacetException;
import org.apache.commons.codec.binary.Base64;
import org.jsound.atomicItems.Base64BinaryItem;
import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.item.Item;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.ItemTypes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.LENGTH;
import static org.jsound.facets.FacetTypes.MAX_LENGTH;
import static org.jsound.facets.FacetTypes.MIN_LENGTH;

public class Base64BinaryType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Arrays.asList(LENGTH, MIN_LENGTH, MAX_LENGTH));

    public Base64BinaryType(String name, AtomicFacets facets) {
        super(ItemTypes.BASE64BINARY, name, facets);
    }

    public Base64BinaryType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.BASE64BINARY, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(Item item, boolean isEnumerationItem) {
        byte[] base64BinaryValue;
        try {
            base64BinaryValue = Base64.decodeBase64(item.getStringValue());
        } catch (Exception e) {
            return false;
        }
        if (this.getFacets() == null)
            return true;
        item = new Base64BinaryItem(base64BinaryValue, item.getStringValue());
        if (!validateLengthFacets(item, isEnumerationItem))
            return false;
        return recursivelyValidate(item);
    }

    @Override
    public void checkBaseType() {
        if (this.subtypeIsValid)
            return;
        AtomicTypeDescriptor baseTypeDescriptor = (AtomicTypeDescriptor) this.baseType.getTypeDescriptor();
        if (!baseTypeDescriptor.isBase64BinaryType())
            throw new LessRestrictiveFacetException("Type " + this.getName() + " is not subtype of " + baseTypeDescriptor
                    .getName());
        areLengthFacetsMoreRestrictive(baseTypeDescriptor.getFacets());
        this.subtypeIsValid = true;
        baseTypeDescriptor.checkBaseType();
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) {
        byte[] base64 = item.getBinaryValue();
        for (Item enumItem : this.getFacets().getEnumeration()) {
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
}
