package org.jsound.atomicTypes;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.jsound.atomicItems.DateItem;
import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.facets.TimezoneFacet;
import org.jsound.item.Item;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.ItemTypes;
import org.jsound.type.TypeDescriptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.joda.time.format.ISODateTimeFormat.dateElementParser;
import static org.jsound.facets.FacetTypes.EXPLICIT_TIMEZONE;
import static org.jsound.facets.FacetTypes.MAX_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MAX_INCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_INCLUSIVE;

public class DateType extends AtomicTypeDescriptor {

    private static final DateTimeParser dtParser = new DateTimeFormatterBuilder().appendOptional(
        ((new DateTimeFormatterBuilder()).appendTimeZoneOffset("Z", true, 2, 4).toFormatter()).getParser()
    ).toParser();
    private static final DateTimeFormatter _formatter = new DateTimeFormatterBuilder().append(dateElementParser())
        .appendOptional(dtParser)
        .toFormatter()
        .withOffsetParsed();

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(
            Arrays.asList(MIN_INCLUSIVE, MAX_INCLUSIVE, MIN_EXCLUSIVE, MAX_EXCLUSIVE, EXPLICIT_TIMEZONE)
    );

    public DateType(String name, AtomicFacets facets) {
        super(ItemTypes.DATE, name, facets);
    }

    public DateType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.DATE, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(Item item, boolean isEnumerationItem) {
        DateTime date;
        try {
            date = getDateFromItem(item);
        } catch (IllegalArgumentException e) {
            return false;
        }
        if (this.getFacets() == null)
            return true;
        item = new DateItem(date);
        if (!validateBoundariesFacets(item, isEnumerationItem))
            return false;
        if (this.getFacets().getDefinedFacets().contains(EXPLICIT_TIMEZONE) && !checkExplicitTimezone(item))
            return false;

        return recursivelyValidate(item);
    }

    private boolean checkExplicitTimezone(Item item) {
        DateTime date = DateTime.parse(
            item.getStringValue(),
            _formatter
        );
        return (item.getStringValue().endsWith("Z")
            || date.getZone() != DateTimeZone.getDefault()
            || !this.getFacets().explicitTimezone.equals(TimezoneFacet.REQUIRED))
            && ((!item.getStringValue().endsWith("Z") && date.getZone() == DateTimeZone.getDefault())
                || !this.getFacets().explicitTimezone.equals(TimezoneFacet.PROHIBITED));
    }

    @Override
    protected boolean validateMinInclusive(Item item) {
        return subtractDate(item.getDateTime(), this.getFacets().minInclusive) >= 0;
    }

    @Override
    protected boolean validateMinExclusive(Item item) {
        return subtractDate(item.getDateTime(), this.getFacets().minExclusive) > 0;
    }

    @Override
    protected boolean validateMaxInclusive(Item item) {
        return subtractDate(item.getDateTime(), this.getFacets().maxInclusive) <= 0;
    }

    @Override
    protected boolean validateMaxExclusive(Item item) {
        return subtractDate(item.getDateTime(), this.getFacets().maxExclusive) < 0;
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) {
        DateTime date = item.getDateTime();
        for (Item enumItem : this.getFacets().getEnumeration()) {
            if (date.equals(getDateFromItem(enumItem)))
                return true;
        }
        return false;
    }

    private long subtractDate(DateTime itemDate, Item constraintItem) {
        return itemDate.getMillis() - getDateFromItem(constraintItem).getMillis();
    }

    private DateTime getDateFromItem(Item item) {
        DateTime date = DateTime.parse(
            item.getStringValue(),
            _formatter
        );
        if (!item.getStringValue().endsWith("Z") && date.getZone() == DateTimeZone.getDefault())
            return date.withZoneRetainFields(DateTimeZone.UTC);
        return date;
    }

    @Override
    public void checkBaseType() {
        checkBoundariesAndTimezoneFacets();
    }

    @Override
    protected boolean isMinInclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MIN_INCLUSIVE)
            &&
            subtractDate(getDateFromItem(this.getFacets().minInclusive), facets.minInclusive) < 0;
    }

    @Override
    protected boolean isMinExclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MIN_EXCLUSIVE)
            &&
            subtractDate(getDateFromItem(this.getFacets().minExclusive), facets.minExclusive) < 0;
    }

    @Override
    protected boolean isMaxInclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MAX_INCLUSIVE)
            &&
            subtractDate(getDateFromItem(this.getFacets().maxInclusive), facets.maxInclusive) > 0;
    }

    @Override
    protected boolean isMaxExclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MAX_EXCLUSIVE)
            &&
            subtractDate(getDateFromItem(this.getFacets().maxExclusive), facets.maxExclusive) > 0;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isDateType() {
        return true;
    }

    @Override
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isDateType();
    }
}
