package org.jsound.atomicTypes;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.jsound.atomicItems.TimeItem;
import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.item.Item;
import org.jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import org.jsound.types.ItemTypes;
import org.jsound.typedescriptors.TypeDescriptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.EXPLICIT_TIMEZONE;
import static org.jsound.facets.FacetTypes.MAX_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MAX_INCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_INCLUSIVE;

public class TimeType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(
            Arrays.asList(MIN_INCLUSIVE, MAX_INCLUSIVE, MIN_EXCLUSIVE, MAX_EXCLUSIVE, EXPLICIT_TIMEZONE)
    );

    public TimeType(String name, AtomicFacets facets) {
        super(ItemTypes.TIME, name, facets);
    }

    public TimeType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.TIME, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(Item item, boolean isEnumValue) {
        DateTime time;
        try {
            time = getTimeFromItem(item);
        } catch (IllegalArgumentException e) {
            return false;
        }
        if (this.getFacets() == null)
            return true;
        item = new TimeItem(time);
        if (!validateBoundariesFacets(item, isEnumValue))
            return false;
        return !this.getFacets().getDefinedFacets().contains(EXPLICIT_TIMEZONE)
            || DateTimeType.checkExplicitTimezone(
                item,
                this.getFacets().explicitTimezone,
                ISODateTimeFormat.timeParser().withOffsetParsed()
            );
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) {
        DateTime time = item.getDateTime();
        for (Item enumItem : this.getFacets().getEnumeration()) {
            if (time.equals(getTimeFromItem(enumItem)))
                return true;
        }
        return false;
    }

    @Override
    protected int compare(Item item1, Item item2) {
        return compareTime(item1, item2);
    }

    private int compareTime(Item timeItem, Item constraintItem) {
        return getTimeFromItem(timeItem).compareTo(getTimeFromItem(constraintItem));
    }

    private DateTime getTimeFromItem(Item item) {
        if (item.isTimeItem())
            return item.getDateTime();
        DateTime time = DateTime.parse(
            item.getStringValue(),
            ISODateTimeFormat.timeParser().withOffsetParsed()
        );
        if (!item.getStringValue().endsWith("Z") && time.getZone() == DateTimeZone.getDefault())
            return time.withZoneRetainFields(DateTimeZone.UTC);
        return time;
    }

    @Override
    public void checkAgainstTypeDescriptor(TypeDescriptor typeDescriptor) {
        checkBoundariesAndTimezoneFacets(typeDescriptor);
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isTimeType() {
        return true;
    }

    @Override
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isTimeType();
    }
}
