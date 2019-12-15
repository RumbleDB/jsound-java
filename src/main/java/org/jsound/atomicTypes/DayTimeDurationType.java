package org.jsound.atomicTypes;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.jsound.atomicItems.DayTimeDurationItem;
import org.jsound.facets.AtomicFacets;
import org.jsound.item.Item;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.ItemTypes;

public class DayTimeDurationType extends DurationType {

    private static final PeriodFormatter _formatter = new PeriodFormatterBuilder().appendLiteral("P")
        .appendDays()
        .appendSuffix("D")
        .appendSeparatorIfFieldsAfter("T")
        .appendHours()
        .appendSuffix("H")
        .appendMinutes()
        .appendSuffix("M")
        .appendSecondsWithOptionalMillis()
        .appendSuffix("S")
        .toFormatter();

    public DayTimeDurationType(String name, AtomicFacets facets) {
        super(ItemTypes.DAYTIMEDURATION, name, facets);
    }

    public DayTimeDurationType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.DAYTIMEDURATION, typeDescriptor);
    }

    @Override
    public boolean validate(Item item) {
        Period period;
        try {
            period = getPeriodFromItem(item);
        } catch (IllegalArgumentException e) {
            return false;
        }
        if (this.getFacets() == null)
            return true;
        item = new DayTimeDurationItem(period);
        if (!validateBoundariesFacets(item))
            return false;
        return this.equals(this.baseType.getTypeDescriptor()) || this.baseType.getTypeDescriptor().validate(item);
    }

    @Override
    protected PeriodFormatter getPeriodFormatter() {
        return _formatter;
    }

    @Override
    public boolean isDayTimeDurationType() {
        return true;
    }
}
