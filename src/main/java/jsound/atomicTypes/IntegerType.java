package jsound.atomicTypes;

import jsound.atomicItems.IntegerItem;
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

import static jsound.facets.FacetTypes.FRACTIONDIGITS;
import static jsound.facets.FacetTypes.MAXEXCLUSIVE;
import static jsound.facets.FacetTypes.MAXINCLUSIVE;
import static jsound.facets.FacetTypes.MINEXCLUSIVE;
import static jsound.facets.FacetTypes.MININCLUSIVE;
import static jsound.facets.FacetTypes.TOTALDIGITS;

public class IntegerType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(
            Arrays.asList(
                MININCLUSIVE,
                MAXINCLUSIVE,
                MINEXCLUSIVE,
                MAXEXCLUSIVE,
                TOTALDIGITS,
                FRACTIONDIGITS
            )
    );

    public IntegerType(String name, AtomicFacets facets) {
        super(ItemTypes.INTEGER, name, facets);
    }

    public IntegerType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.INTEGER, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(ItemWrapper itemWrapper, boolean isEnumValue) {
        Integer integerValue;
        try {
            integerValue = getIntegerFromItem(itemWrapper.getItem());
        } catch (NumberFormatException | UnexpectedTypeException e) {
            return false;
        }
        itemWrapper.setItem(new IntegerItem(integerValue));
        return this.getFacets() == null
            || validateBoundariesFacets(itemWrapper.getItem(), isEnumValue)
                && validateDigitsFacets(
                    itemWrapper.getItem()
                );
    }

    @Override
    protected int compare(Item item1, Item item2) {
        return compareIntegers(item1, item2);
    }

    private static int compareIntegers(Item integerItem, Item constraint) {
        return getIntegerFromItem(integerItem).compareTo(getIntegerFromItem(constraint));
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) {
        Integer integerValue = item.getIntegerValue();
        for (ItemWrapper enumItem : this.getFacets().getEnumeration()) {
            if (integerValue.compareTo(getIntegerFromItem(enumItem.getItem())) == 0)
                return true;
        }
        return false;
    }

    private static Integer getIntegerFromItem(Item item) {
        return item.isStringItem()
            ? Integer.parseInt(item.getStringValue())
            : item.getIntegerValue();
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isIntegerType() {
        return true;
    }

    @Override
    public void checkAgainstTypeDescriptor(TypeDescriptor typeDescriptor) {
        checkBoundariesAndDigitsFacets(typeDescriptor);
    }

    @Override
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isIntegerType() || typeDescriptor.isDecimalType();
    }
}
