package jsound.atomicItems;

import jsound.item.AtomicItem;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class TimeItem extends AtomicItem {
    DateTime _value;
    boolean _hasTimeZone;

    public TimeItem(DateTime value, boolean hasTimeZone) {
        this._value = value;
        this._hasTimeZone = hasTimeZone;
    }

    @Override
    public DateTime getDateTime() {
        return _value;
    }

    @Override
    public String getStringValue() {
        String value = this._value.toString();
        String zoneString = this._value.getZone() == DateTimeZone.UTC ? "Z" : value.substring(value.length() - 6);
        value = value.substring(0, value.length() - zoneString.length());
        value = this._value.getMillisOfSecond() == 0 ? value.substring(0, value.length() - 4) : value;
        int dateTimeSeparatorIndex = value.indexOf("T");
        return value.substring(dateTimeSeparatorIndex + 1) + (_hasTimeZone ? zoneString : "");
    }

    @Override
    public boolean isTimeItem() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TimeItem && this._value.isEqual(((TimeItem) obj).getDateTime());
    }
}
