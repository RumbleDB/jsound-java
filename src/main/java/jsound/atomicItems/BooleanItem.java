package jsound.atomicItems;

import jsound.item.AtomicItem;


public class BooleanItem extends AtomicItem {

    private boolean _value;

    public BooleanItem(boolean value) {
        this._value = value;
    }

    @Override
    public boolean isBoolean() {
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
}
