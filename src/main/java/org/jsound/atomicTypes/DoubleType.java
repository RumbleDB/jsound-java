package org.jsound.atomicTypes;

import org.jsound.atomicItems.DoubleItem;
import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.item.Item;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.ItemTypes;
import org.jsound.type.TypeDescriptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.MAX_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MAX_INCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_INCLUSIVE;

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
    public boolean validate(Item item, boolean isEnumerationItem) {
        Double doubleValue;
        try {
            if (item.isString())
                doubleValue = Double.parseDouble(item.getStringValue());
            else
                doubleValue = item.getDoubleValue();
        } catch (NumberFormatException e) {
            return false;
        }
        if (this.getFacets() == null)
            return true;
        item = new DoubleItem(doubleValue);
        if (!validateBoundariesFacets(item, isEnumerationItem))
            return false;
        return recursivelyValidate(item);
    }

    @Override
    protected boolean validateMinInclusive(Item item) {
        return compareDoubles(item.getDoubleValue(), this.getFacets().minInclusive) >= 0;
    }

    @Override
    protected boolean validateMinExclusive(Item item) {
        return compareDoubles(item.getDoubleValue(), this.getFacets().minExclusive) > 0;
    }

    @Override
    protected boolean validateMaxInclusive(Item item) {
        return compareDoubles(item.getDoubleValue(), this.getFacets().maxInclusive) <= 0;
    }

    @Override
    protected boolean validateMaxExclusive(Item item) {
        return compareDoubles(item.getDoubleValue(), this.getFacets().maxExclusive) < 0;
    }

    private int compareDoubles(Double itemValue, Item constraint) {
        return itemValue.compareTo(getDoubleFromItem(constraint));
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) {
        Double doubleValue = item.getDoubleValue();
        for (Item enumItem : this.getFacets().getEnumeration()) {
            if (doubleValue.compareTo(getDoubleFromItem(enumItem)) == 0)
                return true;
        }
        return false;
    }

    private Double getDoubleFromItem(Item item) {
        return item.isString()
            ? Double.parseDouble(item.getStringValue())
            : item.getDoubleValue();
    }

    @Override
    public void checkBaseType() {
        checkBoundariesFacet();
    }

    @Override
    protected boolean isMinInclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MIN_INCLUSIVE) &&
                compareDoubles(getDoubleFromItem(this.getFacets().minInclusive), facets.minInclusive) < 0;
    }

    @Override
    protected boolean isMinExclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MIN_EXCLUSIVE) &&
                compareDoubles(getDoubleFromItem(this.getFacets().minExclusive), facets.minExclusive) < 0;
    }

    @Override
    protected boolean isMaxInclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MAX_INCLUSIVE) &&
                compareDoubles(getDoubleFromItem(this.getFacets().maxInclusive), facets.maxInclusive) > 0;
    }

    @Override
    protected boolean isMaxExclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MAX_EXCLUSIVE) &&
                compareDoubles(getDoubleFromItem(this.getFacets().maxExclusive), facets.maxExclusive) > 0;
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
