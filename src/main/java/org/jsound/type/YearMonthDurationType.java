package org.jsound.type;

import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.jsound.api.ItemTypes;

public class YearMonthDurationType extends DurationType {

    private static final PeriodFormatter _formatter = new PeriodFormatterBuilder().appendLiteral("P")
        .appendYears()
        .appendSuffix("Y")
        .appendMonths()
        .appendSuffix("M")
        .toFormatter();

    YearMonthDurationType(String typeString) {
        super(ItemTypes.YEARMONTHDURATION, typeString);
    }

    @Override
    public boolean isYearMonthDurationType() {
        return true;
    }

    public static PeriodFormatter getFormatter() {
        return _formatter;
    }
}
