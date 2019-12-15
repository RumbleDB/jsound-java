package org.jsound.atomicItems;

import org.joda.time.DateTime;
import org.jsound.item.AtomicItem;

public class DateTimeItem extends AtomicItem {
    DateTime _value;

    public DateTimeItem(DateTime value) {
        this._value = value;
    }

    @Override
    public DateTime getDateTime() {
        return _value;
    }

    @Override
    public String getStringValue() {
        return this._value.toString();
    }
}
