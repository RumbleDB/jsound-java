package org.jsound.type;

import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.jsound.api.ItemTypes;

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

    DayTimeDurationType(String typeString) {
        super(ItemTypes.DAYTIMEDURATION, typeString);
    }

    @Override
    public boolean isDayTimeDurationType() {
        return true;
    }

    public static PeriodFormatter getFormatter() {
        return _formatter;
    }
}
