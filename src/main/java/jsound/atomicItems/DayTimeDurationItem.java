package jsound.atomicItems;

import org.joda.time.Period;

public class DayTimeDurationItem extends DurationItem {

    public DayTimeDurationItem(Period value) {
        super(value);
    }

    @Override
    public boolean isDayTimeDurationItem() {
        return true;
    }
}
