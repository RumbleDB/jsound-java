package jsound.atomicItems;

import jsound.item.AtomicItem;
import org.api.Item;


public class BooleanItem extends AtomicItem {

    private boolean _value;

    public BooleanItem(boolean value) {
        this._value = value;
    }

    @Override
    public boolean isBooleanItem() {
        return true;
    }

    @Override
    public String getStringValue() {
        return Boolean.toString(this._value);
    }

    @Override
    public String getStringAnnotation() {
        return this.getStringValue();
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(this._value);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof BooleanItem || obj instanceof StringItem)
            && ((Item) obj).getStringValue()
                .equals(this.getStringValue());
    }
}
