package org.jsound.atomicTypes;

import org.jsound.atomicItems.IntegerItem;
import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.item.Item;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.ItemTypes;

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
    public boolean validate(Item item) {
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
        if (!validateBoundariesFacets(item))
            return false;
        if (!validateDigitsFacets(item))
            return false;
        return recursivelyValidate(item);
    }

    @Override
    protected boolean validateMinInclusive(Item item) {
        return compareIntegers(item.getIntegerValue(), this.getFacets().minInclusive) >= 0;
    }

    @Override
    protected boolean validateMinExclusive(Item item) {
        return compareIntegers(item.getIntegerValue(), this.getFacets().minExclusive) > 0;
    }

    @Override
    protected boolean validateMaxInclusive(Item item) {
        return compareIntegers(item.getIntegerValue(), this.getFacets().maxInclusive) <= 0;
    }

    @Override
    protected boolean validateMaxExclusive(Item item) {
        return compareIntegers(item.getIntegerValue(), this.getFacets().maxExclusive) < 0;
    }

    private int compareIntegers(Integer itemValue, Item constraint) {
        return itemValue.compareTo(getIntegerFromItem(constraint));
    }

    @Override
    protected boolean validateEnumeration(Item item) {
        Integer integerValue = item.getIntegerValue();
        for (Item enumItem : this.getFacets().getEnumeration()) {
            if (integerValue.compareTo(getIntegerFromItem(enumItem)) == 0)
                return true;
        }
        return false;
    }

    private Integer getIntegerFromItem(Item item) {
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
}
