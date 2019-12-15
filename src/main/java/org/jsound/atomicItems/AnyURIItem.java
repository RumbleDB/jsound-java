package org.jsound.atomicItems;

import org.jsound.item.AtomicItem;

import java.net.URI;

public class AnyURIItem extends AtomicItem {
    URI _value;

    public AnyURIItem(URI value) {
        this._value = value;
    }

    @Override
    public URI getAnyURIValue() {
        return _value;
    }

    @Override
    public String getStringValue() {
        return this._value.toASCIIString();
    }
}
