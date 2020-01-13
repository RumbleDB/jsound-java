package jsound.atomicTypes;

import jsound.atomicItems.DateItem;
import jsound.atomicItems.DateTimeItem;
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

public class DateType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(
            Arrays.asList(MININCLUSIVE, MAXINCLUSIVE, MINEXCLUSIVE, MAXEXCLUSIVE, EXPLICITTIMEZONE)
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
        return !this.getFacets().getDefinedFacets().contains(EXPLICITTIMEZONE)
            || DateTimeType.checkExplicitTimezone(
                itemWrapper.getItem(),
                this.getFacets().explicitTimezone,
                AtomicTypes.DATE
            );
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
