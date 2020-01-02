package jsound.atomicItems;

import org.joda.time.Period;
import jsound.item.AtomicItem;


public class DurationItem extends AtomicItem {
    Period _value;
    boolean isNegative;

    public DurationItem(Period value) {
        this._value = value;
        isNegative = this._value.toString().contains("-");
    }

    @Override
    public Period getDuration() {
        return _value;
    }

    @Override
    public String getStringValue() {
        if (this.isNegative) {
            return '-' + this._value.negated().toString();
        }
        return this._value.toString();
    }

    @Override
    public boolean isDurationItem() {
        return true;
    }
}
