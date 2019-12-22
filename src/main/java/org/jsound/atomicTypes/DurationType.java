package org.jsound.atomicTypes;

import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.jsound.atomicItems.DurationItem;
import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.item.Item;
import org.jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import org.jsound.types.ItemTypes;
import org.jsound.typedescriptors.TypeDescriptor;

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
    public boolean validate(Item item, boolean isEnumValue) {
        Period period;
        try {
            period = getDurationFromItem(item);
        } catch (IllegalArgumentException e) {
            return false;
        }
        if (this.getFacets() == null)
            return true;
        item = createDurationItem(period);
        return validateBoundariesFacets(item, isEnumValue);
    }

    protected DurationItem createDurationItem(Period period) {
        return new DurationItem(period);
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) {
        Period period = item.getDuration();
        for (Item enumItem : this.getFacets().getEnumeration()) {
            if (period.equals(getDurationFromItem(enumItem)))
                return true;
        }
        return false;
    }

    @Override
    protected int compare(Item item1, Item item2) {
        return compareDuration(item1, item2);
    }

    private int compareDuration(Item durationItem, Item constraintItem) {
        return Long.compare(
            getDurationFromItem(durationItem).minus(getDurationFromItem(constraintItem))
                .toDurationFrom(Instant.now())
                .getMillis(),
            0L
        );
    }

    protected Period getDurationFromItem(Item item) {
        if (item.isDuration())
            return item.getDuration();
        Period period = Period.parse(getPositivePeriod(item.getStringValue()), this.getPeriodFormatter());
        return item.getStringValue().startsWith("-") ? period.negated() : period;
    }

    protected PeriodFormatter getPeriodFormatter() {
        return ISOPeriodFormat.standard();
    }

    @Override
    public void checkAgainstTypeDescriptor(TypeDescriptor typeDescriptor) {
        checkBoundariesFacet(typeDescriptor);
    }

    @Override
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isDurationType();
    }
}
