package jsound.atomicItems;

import org.joda.time.DateTime;
import jsound.item.AtomicItem;
import org.joda.time.DateTimeZone;

public class DateItem extends AtomicItem {
    DateTime _value;
    boolean _hasTimeZone;

    public DateItem(DateTime value, boolean hasTimeZone) {
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
        String zone = this._value.getZone() == DateTimeZone.UTC ? "Z" : this._value.getZone().toString();
        int dateTimeSeparatorIndex = value.indexOf("T");
        return value.substring(0, dateTimeSeparatorIndex) + (_hasTimeZone ? zone : "");
    }

    @Override
    public boolean isDateItem() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DateItem && this._value.isEqual(((DateItem) obj).getDateTime());
    }
}
