package jsound.atomicTypes;

import jsound.atomicItems.DoubleItem;
import jsound.exceptions.UnexpectedTypeException;
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

import static jsound.facets.FacetTypes.MAXEXCLUSIVE;
import static jsound.facets.FacetTypes.MAXINCLUSIVE;
import static jsound.facets.FacetTypes.MINEXCLUSIVE;
import static jsound.facets.FacetTypes.MININCLUSIVE;

public class DoubleType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(
            Arrays.asList(MININCLUSIVE, MAXINCLUSIVE, MINEXCLUSIVE, MAXEXCLUSIVE)
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
            doubleValue = getDoubleFromItem(itemWrapper.getItem());
        } catch (NumberFormatException | UnexpectedTypeException e) {
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
    public boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isDoubleType();
    }
}
