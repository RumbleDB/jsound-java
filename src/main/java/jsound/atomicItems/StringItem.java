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
    public boolean equals(Object obj) {
        return obj instanceof StringItem && this._value.equals(((StringItem) obj)._value);
    }
}
