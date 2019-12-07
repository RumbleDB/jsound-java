package org.jsound.type;

import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.jsound.api.ItemTypes;
import org.jsound.facets.Facets;

public class YearMonthDurationType extends DurationType {

    private static final PeriodFormatter _formatter = new PeriodFormatterBuilder().appendLiteral("P")
        .appendYears()
        .appendSuffix("Y")
        .appendMonths()
        .appendSuffix("M")
        .toFormatter();

    public YearMonthDurationType(String name, Facets facets) {
        super(ItemTypes.YEARMONTHDURATION, name, facets);
        this.baseType = this;
    }

    public static PeriodFormatter getFormatter() {
        return _formatter;
    }
}
