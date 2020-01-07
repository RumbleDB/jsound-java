package jsound.atomicTypes;

import jsound.atomicItems.AnyURIItem;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.types.ItemTypes;
import org.api.Item;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static jsound.facets.FacetTypes.LENGTH;
import static jsound.facets.FacetTypes.MAX_LENGTH;
import static jsound.facets.FacetTypes.MIN_LENGTH;

public class AnyURIType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Arrays.asList(LENGTH, MIN_LENGTH, MAX_LENGTH));

    public AnyURIType(String name, AtomicFacets facets) {
        super(ItemTypes.ANYURI, name, facets);
    }

    public AnyURIType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.ANYURI, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(ItemWrapper itemWrapper, boolean isEnumValue) {
        URI uri;
        if (!itemWrapper.isStringItem())
            return false;
        try {
            uri = URI.create(itemWrapper.getStringValue().replaceAll("\\s+", ""));
        } catch (IllegalArgumentException e) {
            return false;
        }
        itemWrapper.setItem(new AnyURIItem(itemWrapper.getStringValue(), uri));
        return this.getFacets() == null || validateLengthFacets(itemWrapper.getItem(), isEnumValue);
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) throws IllegalArgumentException {
        URI uri = item.getAnyURIValue();
        for (ItemWrapper enumItem : this.getFacets().getEnumeration()) {
            if (uri.equals(URI.create(enumItem.getStringValue().replaceAll("\\s+", ""))))
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
    public boolean isAnyURIType() {
        return true;
    }

    @Override
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isAnyURIType();
    }
}
