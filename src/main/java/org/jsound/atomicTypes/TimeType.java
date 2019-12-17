package org.jsound.atomicTypes;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.jsound.atomicItems.TimeItem;
import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.facets.TimezoneFacet;
import org.jsound.item.Item;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.ItemTypes;

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
    public boolean validate(Item item) {
        DateTime time;
        try {
            time = getTimeFromItem(item);
        } catch (IllegalArgumentException e) {
            return false;
        }
        if (this.getFacets() == null)
            return true;
        item = new TimeItem(time);
        if (!validateBoundariesFacets(item))
            return false;
        if (this.getFacets().getDefinedFacets().contains(EXPLICIT_TIMEZONE) && !checkExplicitTimezone(item))
            return false;
        return recursivelyValidate(item);
    }

    private boolean checkExplicitTimezone(Item item) {
        DateTime time = DateTime.parse(
            item.getStringValue(),
            ISODateTimeFormat.timeParser().withOffsetParsed()
        );
        return (item.getStringValue().endsWith("Z")
            || time.getZone() != DateTimeZone.getDefault()
            || !this.getFacets().explicitTimezone.equals(TimezoneFacet.REQUIRED))
            && ((!item.getStringValue().endsWith("Z") && time.getZone() == DateTimeZone.getDefault())
                || !this.getFacets().explicitTimezone.equals(TimezoneFacet.PROHIBITED));
    }

    @Override
    protected boolean validateMinInclusive(Item item) {
        return subtractTime(item.getDateTime(), this.getFacets().minInclusive) >= 0;
    }

    @Override
    protected boolean validateMinExclusive(Item item) {
        return subtractTime(item.getDateTime(), this.getFacets().minExclusive) > 0;
    }

    @Override
    protected boolean validateMaxInclusive(Item item) {
        return subtractTime(item.getDateTime(), this.getFacets().maxInclusive) <= 0;
    }

    @Override
    protected boolean validateMaxExclusive(Item item) {
        return subtractTime(item.getDateTime(), this.getFacets().maxExclusive) < 0;
    }

    @Override
    protected boolean validateEnumeration(Item item) {
        DateTime time = item.getDateTime();
        for (Item enumItem : this.getFacets().getEnumeration()) {
            if (time.equals(getTimeFromItem(enumItem)))
                return true;
        }
        return false;
    }

    private long subtractTime(DateTime itemTime, Item constraintItem) {
        return itemTime.getMillis() - getTimeFromItem(constraintItem).getMillis();
    }

    private DateTime getTimeFromItem(Item item) {
        DateTime time = DateTime.parse(
            item.getStringValue(),
            ISODateTimeFormat.timeParser().withOffsetParsed()
        );
        if (!item.getStringValue().endsWith("Z") && time.getZone() == DateTimeZone.getDefault())
            return time.withZoneRetainFields(DateTimeZone.UTC);
        return time;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isTimeType() {
        return true;
    }
}
