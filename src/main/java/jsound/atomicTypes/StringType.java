package jsound.atomicTypes;

import org.api.TypeDescriptor;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import org.api.Item;
import jsound.types.ItemTypes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static jsound.facets.FacetTypes.LENGTH;
import static jsound.facets.FacetTypes.MAX_LENGTH;
import static jsound.facets.FacetTypes.MIN_LENGTH;


public class StringType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Arrays.asList(LENGTH, MIN_LENGTH, MAX_LENGTH));

    public StringType(String name, AtomicFacets facets) {
        super(ItemTypes.STRING, name, facets);
    }

    public StringType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.STRING, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(Item item, boolean isEnumValue) {
        return item.isStringItem() && (this.getFacets() == null || validateLengthFacets(item, isEnumValue));
    }


    @Override
    protected boolean validateItemAgainstEnumeration(Item item) {
        String string = item.getStringValue();
        for (Item enumItem : this.getFacets().getEnumeration()) {
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
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isStringType();
    }
}
