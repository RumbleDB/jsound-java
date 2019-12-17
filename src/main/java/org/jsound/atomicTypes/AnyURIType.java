package org.jsound.atomicTypes;

import org.jsound.atomicItems.AnyURIItem;
import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.item.Item;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.ItemTypes;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.LENGTH;
import static org.jsound.facets.FacetTypes.MAX_LENGTH;
import static org.jsound.facets.FacetTypes.MIN_LENGTH;

public class AnyURIType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Arrays.asList(LENGTH, MIN_LENGTH, MAX_LENGTH));

    public AnyURIType(String name, AtomicFacets facets) {
        super(ItemTypes.ANYURI, name, facets);
    }

    public AnyURIType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.ANYURI, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(Item item, boolean isEnumerationItem) {
        URI uri;
        try {
            uri = URI.create(item.getStringValue());
        } catch (IllegalArgumentException e) {
            return false;
        }
        if (this.getFacets() == null)
            return true;
        item = new AnyURIItem(uri);
        if (!validateLengthFacets(item, isEnumerationItem))
            return false;
        return recursivelyValidate(item);
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) throws IllegalArgumentException {
        URI uri = item.getAnyURIValue();
        for (Item enumItem : this.getFacets().getEnumeration()) {
            if (uri.equals(URI.create(enumItem.getStringValue())))
                return true;
        }
        return false;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isAnyURIType() {
        return true;
    }
}
