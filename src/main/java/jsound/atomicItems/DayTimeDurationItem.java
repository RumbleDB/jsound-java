package jsound.atomicItems;

import org.joda.time.Period;
import org.joda.time.PeriodType;

public class DayTimeDurationItem extends DurationItem {

    public DayTimeDurationItem(Period value) {
        super(value.normalizedStandard(PeriodType.dayTime()));
    }

    @Override
    public boolean isDayTimeDurationItem() {
        return true;
    }
}
