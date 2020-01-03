package jsound.atomicItems;

import org.joda.time.Period;

public class YearMonthDurationItem extends DurationItem {

    public YearMonthDurationItem(Period value) {
        super(value);
    }

    @Override
    public boolean isYearMonthDurationItem() {
        return true;
    }
}
