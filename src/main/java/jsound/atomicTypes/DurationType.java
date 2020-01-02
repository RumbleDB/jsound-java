package jsound.atomicTypes;

import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import jsound.atomicItems.DurationItem;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import org.api.Item;
import jsound.types.ItemTypes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static jsound.facets.FacetTypes.MAX_EXCLUSIVE;
import static jsound.facets.FacetTypes.MAX_INCLUSIVE;
import static jsound.facets.FacetTypes.MIN_EXCLUSIVE;
import static jsound.facets.FacetTypes.MIN_INCLUSIVE;

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
    public boolean validate(ItemWrapper itemWrapper, boolean isEnumValue) {
        Period period;
        try {
            period = getDurationFromItem(itemWrapper.getItem());
        } catch (IllegalArgumentException e) {
            return false;
        }
        itemWrapper.setItem(createDurationItem(period));
        return this.getFacets() == null || validateBoundariesFacets(itemWrapper.getItem(), isEnumValue);
    }

    protected DurationItem createDurationItem(Period period) {
        return new DurationItem(period.normalizedStandard(PeriodType.yearMonthDayTime()));
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) {
        Period period = item.getDuration();
        for (ItemWrapper enumItem : this.getFacets().getEnumeration()) {
            if (period.equals(getDurationFromItem(enumItem.getItem())))
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
        if (item.isDurationItem())
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
