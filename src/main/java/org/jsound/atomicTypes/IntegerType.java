package org.jsound.atomicTypes;

import org.jsound.atomicItems.IntegerItem;
import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.item.Item;
import org.jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import org.jsound.types.ItemTypes;
import org.jsound.typedescriptors.TypeDescriptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.FRACTION_DIGITS;
import static org.jsound.facets.FacetTypes.MAX_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MAX_INCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_INCLUSIVE;
import static org.jsound.facets.FacetTypes.TOTAL_DIGITS;

public class IntegerType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(
            Arrays.asList(
                MIN_INCLUSIVE,
                MAX_INCLUSIVE,
                MIN_EXCLUSIVE,
                MAX_EXCLUSIVE,
                TOTAL_DIGITS,
                FRACTION_DIGITS
            )
    );

    public IntegerType(String name, AtomicFacets facets) {
        super(ItemTypes.INTEGER, name, facets);
    }

    public IntegerType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.INTEGER, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(Item item, boolean isEnumValue) {
        Integer integerValue;
        try {
            if (item.isString())
                integerValue = Integer.parseInt(item.getStringValue());
            else
                integerValue = item.getIntegerValue();
        } catch (NumberFormatException e) {
            return false;
        }
        if (this.getFacets() == null)
            return true;
        item = new IntegerItem(integerValue);
        return validateBoundariesFacets(item, isEnumValue) && validateDigitsFacets(item);
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
        for (Item enumItem : this.getFacets().getEnumeration()) {
            if (integerValue.compareTo(getIntegerFromItem(enumItem)) == 0)
                return true;
        }
        return false;
    }

    private static Integer getIntegerFromItem(Item item) {
        return item.isString()
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
