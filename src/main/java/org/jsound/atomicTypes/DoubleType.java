package org.jsound.atomicTypes;

import org.jsound.atomicItems.DoubleItem;
import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.item.Item;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.ItemTypes;

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
    public boolean validate(Item item) {
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
        if (!validateBoundariesFacets(item))
            return false;
        return this.equals(this.baseType.getTypeDescriptor()) || this.baseType.getTypeDescriptor().validate(item);
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
    protected boolean validateEnumeration(Item item) {
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
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isDoubleType() {
        return true;
    }
}
