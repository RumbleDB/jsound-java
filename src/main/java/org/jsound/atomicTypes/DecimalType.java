package org.jsound.atomicTypes;

import org.jsound.atomicItems.DecimalItem;
import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.item.Item;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.ItemTypes;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.FRACTION_DIGITS;
import static org.jsound.facets.FacetTypes.MAX_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MAX_INCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_INCLUSIVE;
import static org.jsound.facets.FacetTypes.TOTAL_DIGITS;

public class DecimalType extends AtomicTypeDescriptor {

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

    public DecimalType(String name, AtomicFacets facets) {
        super(ItemTypes.DECIMAL, name, facets);
    }

    public DecimalType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.DECIMAL, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(Item item) {
        BigDecimal decimalValue;
        try {
            if (item.isString())
                decimalValue = new BigDecimal(item.getStringValue());
            else
                decimalValue = item.getDecimalValue();
        } catch (NumberFormatException e) {
            return false;
        }
        if (this.getFacets() == null)
            return true;
        item = new DecimalItem(decimalValue);
        if (!validateBoundariesFacets(item))
            return false;
        if (!validateDigitsFacets(item))
            return false;
        return recursivelyValidate(item);
    }

    @Override
    protected boolean validateMinInclusive(Item item) {
        return compareDecimals(item.getDecimalValue(), this.getFacets().minInclusive) >= 0;
    }

    @Override
    protected boolean validateMinExclusive(Item item) {
        return compareDecimals(item.getDecimalValue(), this.getFacets().minExclusive) > 0;
    }

    @Override
    protected boolean validateMaxInclusive(Item item) {
        return compareDecimals(item.getDecimalValue(), this.getFacets().maxInclusive) <= 0;
    }

    @Override
    protected boolean validateMaxExclusive(Item item) {
        return compareDecimals(item.getDecimalValue(), this.getFacets().maxExclusive) < 0;
    }

    private int compareDecimals(BigDecimal itemValue, Item constraint) {
        return itemValue.compareTo(getDecimalFromItem(constraint));
    }

    @Override
    protected boolean validateEnumeration(Item item) {
        BigDecimal decimalValue = item.getDecimalValue();
        for (Item enumItem : this.getFacets().getEnumeration()) {
            if (decimalValue.compareTo(getDecimalFromItem(enumItem)) == 0)
                return true;
        }
        return false;
    }

    private BigDecimal getDecimalFromItem(Item item) {
        return item.isString()
            ? new BigDecimal(item.getStringValue())
            : item.getDecimalValue();
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isDecimalType() {
        return true;
    }
}
