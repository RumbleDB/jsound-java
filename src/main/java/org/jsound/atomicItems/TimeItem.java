package org.jsound.atomicItems;

import org.joda.time.DateTime;
import org.jsound.item.AtomicItem;

public class TimeItem extends AtomicItem {
    DateTime _value;

    public TimeItem(DateTime value) {
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
