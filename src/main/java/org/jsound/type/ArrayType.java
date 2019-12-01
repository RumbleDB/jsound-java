package org.jsound.type;

import org.jsound.api.ItemType;

public class ArrayType extends ItemType {
    private ItemType arrayItemsType;

    ArrayType(ItemType arrayItemsType) {
        this.arrayItemsType = arrayItemsType;
    }
}
