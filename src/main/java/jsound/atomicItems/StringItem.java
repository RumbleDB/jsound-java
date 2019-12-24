package jsound.atomicItems;

import jsound.item.AtomicItem;


public class StringItem extends AtomicItem {

    private String _value;

    public StringItem(String string) {
        this._value = string;
    }

    @Override
    public boolean isStringItem() {
        return true;
    }

    @Override
    public String getStringValue() {
        return this._value;
    }

    @Override
    public int hashCode() {
        return this._value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StringItem))
            return false;
        return this._value.equals(((StringItem) o)._value);
    }
}
