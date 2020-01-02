package jsound.atomicItems;

import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;

public class YearMonthDurationItem extends DurationItem {
    private static final PeriodType yearMonthPeriodType = PeriodType.forFields(
            new DurationFieldType[] { DurationFieldType.years(), DurationFieldType.months() }
    );

    public YearMonthDurationItem(Period value) {
        super(value.normalizedStandard(yearMonthPeriodType));
    }

    @Override
    public boolean isYearMonthDurationItem() {
        return true;
    }
}
