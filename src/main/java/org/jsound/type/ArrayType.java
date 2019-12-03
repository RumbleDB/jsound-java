package org.jsound.type;

import org.jsound.api.ItemType;
import org.jsound.api.ItemTypes;

public class ArrayType extends ItemType {
    private ItemType _arrayItemsType;

    ArrayType(ItemType arrayItemsType) {
        super(ItemTypes.ARRAY);
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
