package org.jsound.atomicItems;

import org.joda.time.Period;
import org.jsound.item.AtomicItem;


public class DurationItem extends AtomicItem {
    Period _value;

    public DurationItem(Period value) {
        this._value = value;
    }

    @Override
    public Period getDuration() {
        return _value;
    }

    @Override
    public String getStringValue() {
        return this._value.toString();
    }
}
