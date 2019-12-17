package org.jsound.atomicTypes;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.jsound.atomicItems.DurationItem;
import org.jsound.atomicItems.YearMonthDurationItem;
import org.jsound.facets.AtomicFacets;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.ItemTypes;

public class YearMonthDurationType extends DurationType {

    private static final PeriodFormatter _formatter = new PeriodFormatterBuilder().appendLiteral("P")
        .appendYears()
        .appendSuffix("Y")
        .appendMonths()
        .appendSuffix("M")
        .toFormatter();

    public YearMonthDurationType(String name, AtomicFacets facets) {
        super(ItemTypes.YEARMONTHDURATION, name, facets);
    }

    public YearMonthDurationType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.YEARMONTHDURATION, typeDescriptor);
    }

    @Override
    protected DurationItem createDurationItem(Period period) {
        return new YearMonthDurationItem(period);
    }

    @Override
    protected PeriodFormatter getPeriodFormatter() {
        return _formatter;
    }

    @Override
    public boolean isYearMonthDurationType() {
        return true;
    }
}
