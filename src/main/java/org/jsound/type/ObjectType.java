package org.jsound.type;

import org.jsound.api.ItemType;

import java.util.Map;

public class ObjectType extends ItemType {

    private Map<ObjectKey, ItemType> _typeMap;

    ObjectType(Map<ObjectKey, ItemType> typeMap) {
        this._typeMap = typeMap;
    }
}
