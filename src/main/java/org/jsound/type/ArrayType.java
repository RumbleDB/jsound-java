package org.jsound.type;

import org.jsound.api.ItemType;

public class ArrayType extends ItemType {
    private ItemType _arrayItemsType;

    ArrayType(ItemType arrayItemsType) {
        this._arrayItemsType = arrayItemsType;
    }

    public ItemType getArrayItemsType() {
        return this._arrayItemsType;
    }

    @Override
    public boolean isArrayType() {
        return true;
    }
}
