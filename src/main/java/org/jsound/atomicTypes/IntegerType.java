package org.jsound.atomicTypes;

import jsound.exceptions.LessRestrictiveFacetException;
import org.jsound.atomicItems.IntegerItem;
import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.item.Item;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.ItemTypes;
import org.jsound.type.TypeDescriptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.ENUMERATION;
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
    public boolean validate(Item item, boolean isEnumerationItem) {
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
        if (!validateBoundariesFacets(item, isEnumerationItem))
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

    private static int compareIntegers(Integer itemValue, Item constraint) {
        return itemValue.compareTo(getIntegerFromItem(constraint));
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
    public void isSubtypeOf(TypeDescriptor typeDescriptor) {
        if (typeDescriptor == null)
            this.subtypeIsValid = true;
        if (this.subtypeIsValid)
            return;
        if (!typeDescriptor.isIntegerType() && !typeDescriptor.isDecimalType())
            throw new LessRestrictiveFacetException("Type " + this.getName() + " is not subtype of " + typeDescriptor.getName());
        areBoundariesMoreRestrictive(((AtomicTypeDescriptor) typeDescriptor).getFacets());
        areDigitsFacetsMoreRestrictive(((AtomicTypeDescriptor) typeDescriptor).getFacets());
        this.subtypeIsValid = true;
        if (this.baseType != null)
            typeDescriptor.isSubtypeOf(typeDescriptor.baseType.getTypeDescriptor());
    }

    @Override
    protected boolean isMinInclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MIN_INCLUSIVE) &&
                compareIntegers(getIntegerFromItem(this.getFacets().minInclusive), facets.minInclusive) < 0;
    }

    @Override
    protected boolean isMinExclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MIN_EXCLUSIVE) &&
                compareIntegers(getIntegerFromItem(this.getFacets().minExclusive), facets.minExclusive) <= 0;
    }

    @Override
    protected boolean isMaxInclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MAX_INCLUSIVE) &&
                compareIntegers(getIntegerFromItem(this.getFacets().maxInclusive), facets.maxInclusive) > 0;
    }

    @Override
    protected boolean isMaxExclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MAX_EXCLUSIVE) &&
                compareIntegers(getIntegerFromItem(this.getFacets().maxExclusive), facets.maxExclusive) >= 0;
    }

    @Override
    protected boolean isEnumerationMoreRestrictive(AtomicFacets facets) {
        if (!facets.getDefinedFacets().contains(ENUMERATION))
            return enumerationRestrictsMinInclusive(facets);
        for (Item item : this.getFacets().getEnumeration()) {
            if (!facets.getEnumeration().contains(item))
                return false;
        }
        return true;
    }

    private boolean enumerationRestrictsMinInclusive(AtomicFacets facets) {
        if (facets.getDefinedFacets().contains(MIN_INCLUSIVE)) {
            for (Item item : this.getFacets().getEnumeration()) {
                if (compareIntegers(getIntegerFromItem(item), facets.minInclusive) < 0)
                    return false;
            }
        }
        return enumerationRestrictsMinExclusive(facets);
    }

    private boolean enumerationRestrictsMinExclusive(AtomicFacets facets) {
        if (facets.getDefinedFacets().contains(MIN_EXCLUSIVE)) {
            for (Item item : this.getFacets().getEnumeration()) {
                if (compareIntegers(getIntegerFromItem(item), facets.minExclusive) <= 0)
                    return false;
            }
        }
        return enumerationRestrictsMaxInclusive(facets);
    }

    private boolean enumerationRestrictsMaxInclusive(AtomicFacets facets) {
        if (facets.getDefinedFacets().contains(MAX_INCLUSIVE)) {
            for (Item item : this.getFacets().getEnumeration()) {
                if (compareIntegers(getIntegerFromItem(item), facets.maxInclusive) > 0)
                    return false;
            }
        }
        return enumerationRestrictsMaxExclusive(facets);
    }

    private boolean enumerationRestrictsMaxExclusive(AtomicFacets facets) {
        if (facets.getDefinedFacets().contains(MAX_EXCLUSIVE)) {
            for (Item item : this.getFacets().getEnumeration()) {
                if (compareIntegers(getIntegerFromItem(item), facets.maxExclusive) >= 0)
                    return false;
            }
        }
        return true;
    }

    @Override
    protected boolean isTotalDigitsMoreRestrictive(AtomicFacets facets) {
        return !facets.getDefinedFacets().contains(TOTAL_DIGITS)
                || facets.totalDigits.equals(this.getFacets().totalDigits);
    }

    @Override
    protected boolean isFractionDigitsMoreRestrictive(AtomicFacets facets) {
        return !facets.getDefinedFacets().contains(FRACTION_DIGITS)
                || facets.fractionDigits.equals(this.getFacets().fractionDigits);
    }
}
