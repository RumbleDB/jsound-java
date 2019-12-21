package org.jsound.atomicTypes;

import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.jsound.atomicItems.DurationItem;
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

public class DurationType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(
            Arrays.asList(MIN_INCLUSIVE, MAX_INCLUSIVE, MIN_EXCLUSIVE, MAX_EXCLUSIVE)
    );

    public DurationType(String name, AtomicFacets facets) {
        super(ItemTypes.DURATION, name, facets);
    }

    public DurationType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.DURATION, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    DurationType(ItemTypes durationSubtype, String name, AtomicFacets facets) {
        super(durationSubtype, name, facets);
    }

    DurationType(ItemTypes durationSubtype, AtomicTypeDescriptor typeDescriptor) {
        super(durationSubtype, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    public static String getPositivePeriod(String period) {
        if (period.startsWith("-"))
            return period.substring(1);
        return period;
    }

    @Override
    public boolean isDurationType() {
        return true;
    }

    @Override
    public boolean validate(Item item, boolean isEnumerationItem) {
        Period period;
        try {
            period = getPeriodFromItem(item);
        } catch (IllegalArgumentException e) {
            return false;
        }
        if (this.getFacets() == null)
            return true;
        item = createDurationItem(period);
        if (!validateBoundariesFacets(item, isEnumerationItem))
            return false;
        return recursivelyValidate(item);
    }

    protected DurationItem createDurationItem(Period period) {
        return new DurationItem(period);
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) {
        Period period = item.getDuration();
        for (Item enumItem : this.getFacets().getEnumeration()) {
            if (period.equals(getPeriodFromItem(enumItem)))
                return true;
        }
        return false;
    }

    @Override
    protected boolean validateMinInclusive(Item item) {
        return subtractPeriods(item.getDuration(), this.getFacets().minInclusive) >= 0;
    }

    @Override
    protected boolean validateMinExclusive(Item item) {
        return subtractPeriods(item.getDuration(), this.getFacets().minExclusive) > 0;
    }

    @Override
    protected boolean validateMaxInclusive(Item item) {
        return subtractPeriods(item.getDuration(), this.getFacets().maxInclusive) <= 0;
    }

    @Override
    protected boolean validateMaxExclusive(Item item) {
        return subtractPeriods(item.getDuration(), this.getFacets().maxExclusive) < 0;
    }

    private long subtractPeriods(Period itemPeriod, Item constraintItem) {
        return itemPeriod.minus(getPeriodFromItem(constraintItem)).toDurationFrom(Instant.now()).getMillis();
    }

    protected Period getPeriodFromItem(Item item) {
        Period period = Period.parse(getPositivePeriod(item.getStringValue()), this.getPeriodFormatter());
        return item.getStringValue().startsWith("-") ? period.negated() : period;
    }

    protected PeriodFormatter getPeriodFormatter() {
        return ISOPeriodFormat.standard();
    }

    @Override
    public void checkBaseType() {
        checkBoundariesFacet();
    }

    @Override
    protected boolean isMinInclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MIN_INCLUSIVE) &&
                subtractPeriods(getPeriodFromItem(this.getFacets().minInclusive), facets.minInclusive) < 0;
    }

    @Override
    protected boolean isMinExclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MIN_EXCLUSIVE) &&
                subtractPeriods(getPeriodFromItem(this.getFacets().minExclusive), facets.minExclusive) < 0;
    }

    @Override
    protected boolean isMaxInclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MAX_INCLUSIVE) &&
                subtractPeriods(getPeriodFromItem(this.getFacets().maxInclusive), facets.maxInclusive) > 0;
    }

    @Override
    protected boolean isMaxExclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MAX_EXCLUSIVE) &&
                subtractPeriods(getPeriodFromItem(this.getFacets().maxExclusive), facets.maxExclusive) > 0;
    }

    @Override
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isDurationType();
    }
}
