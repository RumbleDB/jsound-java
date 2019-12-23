package org.jsound.atomicItems;

import org.jsound.item.AtomicItem;

import java.util.Arrays;

public class HexBinaryItem extends AtomicItem {
    byte[] _value;
    String _stringValue;

    public HexBinaryItem(byte[] _value, String _stringValue) {
        this._value = _value;
        this._stringValue = _stringValue;
    }

    @Override
    public byte[] getBinaryValue() {
        return _value;
    }

    @Override
    public String getStringValue() {
        return Arrays.toString(this._value);
    }
}
