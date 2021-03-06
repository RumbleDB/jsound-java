package jsound.atomicItems;

import jsound.item.AtomicItem;

import java.net.URI;

public class AnyURIItem extends AtomicItem {
    String _stringValue;
    URI _value;

    public AnyURIItem(String stringValue, URI value) {
        this._stringValue = stringValue;
        this._value = value;
    }

    @Override
    public boolean isAnyURIItem() {
        return true;
    }

    @Override
    public URI getAnyURIValue() {
        return _value;
    }

    @Override
    public String getStringValue() {
        return this._stringValue;
    }

    @Override
    public int hashCode() {
        return this._value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AnyURIItem))
            return false;
        AnyURIItem anyURIItem = (AnyURIItem) obj;
        return this._value.equals(anyURIItem.getAnyURIValue());
    }
}
