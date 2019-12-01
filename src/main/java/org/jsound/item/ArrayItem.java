package org.jsound.item;

import org.jsound.api.Item;

import java.util.List;

public class ArrayItem extends Item {

    private List<Item> _arrayItems;

    ArrayItem(List<Item> arrayItems) {
        super();
        this._arrayItems = arrayItems;
    }
}
