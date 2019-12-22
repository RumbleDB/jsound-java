package org.jsound.atomicTypes;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.jsound.atomicItems.DateItem;
import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.item.Item;
import org.jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import org.jsound.types.ItemTypes;
import org.jsound.typedescriptors.TypeDescriptor;

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
    public boolean validate(Item item, boolean isEnumValue) {
        DateTime date;
        try {
            date = getDateFromItem(item);
        } catch (IllegalArgumentException e) {
            return false;
        }
        if (this.getFacets() == null)
            return true;
        item = new DateItem(date);
        if (!validateBoundariesFacets(item, isEnumValue))
            return false;
        return !this.getFacets().getDefinedFacets().contains(EXPLICIT_TIMEZONE)
            || DateTimeType.checkExplicitTimezone(item, this.getFacets().explicitTimezone, _formatter);
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

    @Override
    protected int compare(Item item1, Item item2) {
        return compareDate(item1, item2);
    }

    private int compareDate(Item dateItem, Item constraintItem) {
        return getDateFromItem(dateItem).compareTo(getDateFromItem(constraintItem));
    }

    private DateTime getDateFromItem(Item item) {
        if (item.isDateItem())
            return item.getDateTime();
        DateTime date = DateTime.parse(
            item.getStringValue(),
            _formatter
        );
        if (!item.getStringValue().endsWith("Z") && date.getZone() == DateTimeZone.getDefault())
            return date.withZoneRetainFields(DateTimeZone.UTC);
        return date;
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
    public boolean isDateType() {
        return true;
    }

    @Override
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isDateType();
    }
}
