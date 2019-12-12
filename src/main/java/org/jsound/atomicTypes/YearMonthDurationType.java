package org.jsound.atomicTypes;

import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.jsound.facets.Facets;
import org.jsound.type.ItemTypes;

public class YearMonthDurationType extends DurationType {

    private static final PeriodFormatter _formatter = new PeriodFormatterBuilder().appendLiteral("P")
        .appendYears()
        .appendSuffix("Y")
        .appendMonths()
        .appendSuffix("M")
        .toFormatter();

    public YearMonthDurationType(String name, Facets facets) {
        super(ItemTypes.YEARMONTHDURATION, name, facets);
    }

    public static PeriodFormatter getFormatter() {
        return _formatter;
    }

    @Override
    public boolean isYearMonthDurationType() {
        return true;
    }
}
