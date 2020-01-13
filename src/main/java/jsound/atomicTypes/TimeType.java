package jsound.atomicTypes;

import jsound.atomicItems.DateTimeItem;
import jsound.atomicItems.TimeItem;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.types.AtomicTypes;
import jsound.types.ItemTypes;
import org.api.Item;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static jsound.facets.FacetTypes.EXPLICITTIMEZONE;
import static jsound.facets.FacetTypes.MAXEXCLUSIVE;
import static jsound.facets.FacetTypes.MAXINCLUSIVE;
import static jsound.facets.FacetTypes.MINEXCLUSIVE;
import static jsound.facets.FacetTypes.MININCLUSIVE;

public class TimeType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(
            Arrays.asList(MININCLUSIVE, MAXINCLUSIVE, MINEXCLUSIVE, MAXEXCLUSIVE, EXPLICITTIMEZONE)
    );

    public TimeType(String name, AtomicFacets facets) {
        super(ItemTypes.TIME, name, facets);
    }

    public TimeType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.TIME, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(ItemWrapper itemWrapper, boolean isEnumValue) {
        try {
            itemWrapper.setItem(getTimeFromItem(itemWrapper.getItem()));
        } catch (IllegalArgumentException e) {
            return false;
        }
        if (this.getFacets() == null)
            return true;
        if (!validateBoundariesFacets(itemWrapper.getItem(), isEnumValue))
            return false;
        return !this.getFacets().getDefinedFacets().contains(EXPLICITTIMEZONE)
            || DateTimeType.checkExplicitTimezone(
                itemWrapper.getItem(),
                this.getFacets().explicitTimezone,
                AtomicTypes.TIME
            );
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) {
        DateTime time = item.getDateTime();
        for (ItemWrapper enumItem : this.getFacets().getEnumeration()) {
            if (time.equals(getTimeFromItem(enumItem.getItem()).getDateTime()))
                return true;
        }
        return false;
    }

    @Override
    protected int compare(Item item1, Item item2) {
        return compareTime(item1, item2);
    }

    private int compareTime(Item timeItem, Item constraintItem) {
        return getTimeFromItem(timeItem).getDateTime().compareTo(getTimeFromItem(constraintItem).getDateTime());
    }

    private Item getTimeFromItem(Item item) {
        if (item.isTimeItem())
            return item;
        DateTime time = DateTimeItem.parseDateTime(item.getStringValue(), AtomicTypes.TIME);
        if (!item.getStringValue().endsWith("Z") && time.getZone() == DateTimeZone.getDefault()) {
            return new TimeItem(time.withZoneRetainFields(DateTimeZone.UTC), false);
        }
        return new TimeItem(time, true);
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
