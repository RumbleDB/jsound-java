package org.jsound.type;

import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.jsound.api.ItemTypes;
import org.jsound.facets.Facets;

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

    public DayTimeDurationType(String name, Facets facets) {
        super(ItemTypes.DAYTIMEDURATION, name, facets);
        this.baseType = this;
    }

    public static PeriodFormatter getFormatter() {
        return _formatter;
    }
}
