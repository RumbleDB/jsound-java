package org.jsound.item;

import org.jsound.api.Item;
import org.jsound.api.ItemType;

import java.util.Map;

public class ObjectItem extends Item {
    private Map<String, Item> _typeMap;

    ObjectItem(Map<String, Item> typeMap) {
        this._typeMap = typeMap;
    }
}
