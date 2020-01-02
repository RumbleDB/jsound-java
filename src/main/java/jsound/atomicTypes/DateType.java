package jsound.atomicTypes;

import jsound.atomicItems.DateTimeItem;
import jsound.types.AtomicTypes;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import jsound.atomicItems.DateItem;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import org.api.Item;
import jsound.types.ItemTypes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.joda.time.format.ISODateTimeFormat.dateElementParser;
import static jsound.facets.FacetTypes.EXPLICIT_TIMEZONE;
import static jsound.facets.FacetTypes.MAX_EXCLUSIVE;
import static jsound.facets.FacetTypes.MAX_INCLUSIVE;
import static jsound.facets.FacetTypes.MIN_EXCLUSIVE;
import static jsound.facets.FacetTypes.MIN_INCLUSIVE;

public class DateType extends AtomicTypeDescriptor {

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
    public boolean validate(ItemWrapper itemWrapper, boolean isEnumValue) {
        try {
            itemWrapper.setItem(getDateFromItem(itemWrapper.getItem()));
        } catch (IllegalArgumentException e) {
            return false;
        }
        if (this.getFacets() == null)
            return true;
        if (!validateBoundariesFacets(itemWrapper.getItem(), isEnumValue))
            return false;
        return !this.getFacets().getDefinedFacets().contains(EXPLICIT_TIMEZONE)
            || DateTimeType.checkExplicitTimezone(itemWrapper.getItem(), this.getFacets().explicitTimezone, AtomicTypes.DATE);
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) {
        DateTime date = item.getDateTime();
        for (ItemWrapper enumItem : this.getFacets().getEnumeration()) {
            if (date.equals(getDateFromItem(enumItem.getItem()).getDateTime()))
                return true;
        }
        return false;
    }

    @Override
    protected int compare(Item item1, Item item2) {
        return compareDate(item1, item2);
    }

    private int compareDate(Item dateItem, Item constraintItem) {
        return getDateFromItem(dateItem).getDateTime().compareTo(getDateFromItem(constraintItem).getDateTime());
    }

    private Item getDateFromItem(Item item) {
        if (item.isDateItem())
            return item;
        DateTime date = DateTimeItem.parseDateTime(item.getStringValue(), AtomicTypes.DATE);
        if (!item.getStringValue().endsWith("Z") && date.getZone() == DateTimeZone.getDefault()) {
            return new DateItem(date.withZoneRetainFields(DateTimeZone.UTC), false);
        }
        return new DateItem(date, true);
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
