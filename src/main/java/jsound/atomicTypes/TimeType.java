package jsound.atomicTypes;

import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import jsound.atomicItems.TimeItem;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
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
    public boolean validate(ItemWrapper itemWrapper, boolean isEnumValue) {
        DateTime time;
        try {
            time = getTimeFromItem(itemWrapper.getItem());
        } catch (IllegalArgumentException e) {
            return false;
        }
        itemWrapper.setItem(new TimeItem(time));
        if (this.getFacets() == null)
            return true;
        if (!validateBoundariesFacets(itemWrapper.getItem(), isEnumValue))
            return false;
        return !this.getFacets().getDefinedFacets().contains(EXPLICIT_TIMEZONE)
            || DateTimeType.checkExplicitTimezone(
                itemWrapper.getItem(),
                this.getFacets().explicitTimezone,
                ISODateTimeFormat.timeParser().withOffsetParsed()
            );
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) {
        DateTime time = item.getDateTime();
        for (ItemWrapper enumItem : this.getFacets().getEnumeration()) {
            if (time.equals(getTimeFromItem(enumItem.getItem())))
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
