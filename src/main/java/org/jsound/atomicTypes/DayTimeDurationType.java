package org.jsound.atomicTypes;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.jsound.atomicItems.DayTimeDurationItem;
import org.jsound.atomicItems.DurationItem;
import org.jsound.facets.AtomicFacets;
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
    protected DurationItem createDurationItem(Period period) {
        return new DayTimeDurationItem(period);
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
