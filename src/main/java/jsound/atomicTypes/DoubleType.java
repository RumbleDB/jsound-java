package jsound.atomicTypes;

import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.atomicItems.DoubleItem;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import org.api.Item;
import jsound.types.ItemTypes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static jsound.facets.FacetTypes.MAX_EXCLUSIVE;
import static jsound.facets.FacetTypes.MAX_INCLUSIVE;
import static jsound.facets.FacetTypes.MIN_EXCLUSIVE;
import static jsound.facets.FacetTypes.MIN_INCLUSIVE;

public class DoubleType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(
            Arrays.asList(MIN_INCLUSIVE, MAX_INCLUSIVE, MIN_EXCLUSIVE, MAX_EXCLUSIVE)
    );

    public DoubleType(String name, AtomicFacets facets) {
        super(ItemTypes.DOUBLE, name, facets);
    }

    public DoubleType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.DOUBLE, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(ItemWrapper itemWrapper, boolean isEnumValue) {
        Double doubleValue;
        try {
            if (itemWrapper.isStringItem())
                doubleValue = Double.parseDouble(itemWrapper.getStringValue());
            else
                doubleValue = itemWrapper.getDoubleValue();
        } catch (NumberFormatException e) {
            return false;
        }
        itemWrapper.setItem(new DoubleItem(doubleValue));
        return this.getFacets() == null || validateBoundariesFacets(itemWrapper.getItem(), isEnumValue);
    }

    @Override
    protected int compare(Item item1, Item item2) {
        return compareDoubles(item1, item2);
    }

    private int compareDoubles(Item item, Item constraint) {
        return getDoubleFromItem(item).compareTo(getDoubleFromItem(constraint));
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) {
        Double doubleValue = item.getDoubleValue();
        for (ItemWrapper enumItem : this.getFacets().getEnumeration()) {
            if (doubleValue.compareTo(getDoubleFromItem(enumItem.getItem())) == 0)
                return true;
        }
        return false;
    }

    private Double getDoubleFromItem(Item item) {
        return item.isStringItem()
            ? Double.parseDouble(item.getStringValue())
            : item.getDoubleValue();
    }

    @Override
    public void checkAgainstTypeDescriptor(TypeDescriptor typeDescriptor) {
        checkBoundariesFacet(typeDescriptor);
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isDoubleType() {
        return true;
    }

    @Override
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isDoubleType();
    }
}
