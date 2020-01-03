package jsound.atomicItems;

import org.api.Item;
import org.joda.time.Instant;
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Item)) {
            return false;
        }
        Item item = (Item) obj;
        Instant now = new Instant();
        if (item.isDurationItem()) {
            return this.getDuration()
                .toDurationFrom(now)
                .isEqual(item.getDuration().toDurationFrom(now));
        }
        return false;
    }

}
