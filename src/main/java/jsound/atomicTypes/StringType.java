package jsound.atomicTypes;

import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.types.ItemTypes;
import org.api.Item;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static jsound.facets.FacetTypes.LENGTH;
import static jsound.facets.FacetTypes.MAXLENGTH;
import static jsound.facets.FacetTypes.MINLENGTH;


public class StringType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Arrays.asList(LENGTH, MINLENGTH, MAXLENGTH));

    public StringType(String name, AtomicFacets facets) {
        super(ItemTypes.STRING, name, facets);
    }

    public StringType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.STRING, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(ItemWrapper itemWrapper, boolean isEnumValue) {
        return itemWrapper.isStringItem()
            && (this.getFacets() == null || validateLengthFacets(itemWrapper.getItem(), isEnumValue));
    }


    @Override
    protected boolean validateItemAgainstEnumeration(Item item) {
        String string = item.getStringValue();
        for (ItemWrapper enumItem : this.getFacets().getEnumeration()) {
            if (string.equals(enumItem.getStringValue()))
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
    public boolean isStringType() {
        return true;
    }

    @Override
    public boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isStringType();
    }
}
