package jsound.atomicTypes;

import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import jsound.atomicItems.DateTimeItem;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import jsound.facets.TimezoneFacet;
import org.api.Item;
import jsound.types.ItemTypes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static jsound.facets.FacetTypes.EXPLICIT_TIMEZONE;
import static jsound.facets.FacetTypes.MAX_EXCLUSIVE;
import static jsound.facets.FacetTypes.MAX_INCLUSIVE;
import static jsound.facets.FacetTypes.MIN_EXCLUSIVE;
import static jsound.facets.FacetTypes.MIN_INCLUSIVE;

public class DateTimeType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(
            Arrays.asList(MIN_INCLUSIVE, MAX_INCLUSIVE, MIN_EXCLUSIVE, MAX_EXCLUSIVE, EXPLICIT_TIMEZONE)
    );

    public DateTimeType(String name, AtomicFacets facets) {
        super(ItemTypes.DATETIME, name, facets);
    }

    public DateTimeType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.DATETIME, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(ItemWrapper itemWrapper, boolean isEnumValue) {
        DateTime dateTime;
        try {
            dateTime = getDateTimeFromItem(itemWrapper.getItem());
        } catch (IllegalArgumentException e) {
            return false;
        }
        itemWrapper.setItem(new DateTimeItem(dateTime));
        if (this.getFacets() == null)
            return true;
        if (!validateBoundariesFacets(itemWrapper.getItem(), isEnumValue))
            return false;
        return !this.getFacets().getDefinedFacets().contains(EXPLICIT_TIMEZONE)
            || checkExplicitTimezone(
                itemWrapper.getItem(),
                this.getFacets().explicitTimezone,
                ISODateTimeFormat.dateTimeParser().withOffsetParsed()
            );
    }

    static boolean checkExplicitTimezone(Item item, TimezoneFacet explicitTimezone, DateTimeFormatter formatter) {
        DateTime dateTime = DateTime.parse(
            item.getStringValue(),
            formatter
        );
        return ((!item.getStringValue().endsWith("Z") && dateTime.getZone() == DateTimeZone.getDefault())
            || !explicitTimezone.equals(TimezoneFacet.PROHIBITED))
            && ((item.getStringValue().endsWith("Z")
                || dateTime.getZone() != DateTimeZone.getDefault())
                || !explicitTimezone.equals(TimezoneFacet.REQUIRED));
    }

    @Override
    protected int compare(Item item1, Item item2) {
        return compareDateTime(item1, item2);
    }

    private int compareDateTime(Item dateTimeItem, Item constraintItem) {
        return getDateTimeFromItem(dateTimeItem).compareTo(getDateTimeFromItem(constraintItem));
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) {
        DateTime dateTime = item.getDateTime();
        for (ItemWrapper enumItem : this.getFacets().getEnumeration()) {
            if (dateTime.equals(getDateTimeFromItem(enumItem.getItem())))
                return true;
        }
        return false;
    }

    private DateTime getDateTimeFromItem(Item item) {
        if (item.isDateTimeItem())
            return item.getDateTime();
        DateTime dateTime = DateTime.parse(
            item.getStringValue(),
            ISODateTimeFormat.dateTimeParser().withOffsetParsed()
        );
        if (!item.getStringValue().endsWith("Z") && dateTime.getZone() == DateTimeZone.getDefault())
            return dateTime.withZoneRetainFields(DateTimeZone.UTC);
        return dateTime;
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
    public boolean isDateTimeType() {
        return true;
    }

    @Override
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isDateTimeType();
    }
}
