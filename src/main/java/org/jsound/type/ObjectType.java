package org.jsound.type;

import org.jsound.api.ItemType;
import org.jsound.api.ItemTypes;

import java.util.Map;

public class ObjectType extends ItemType {

    private Map<ObjectKey, ItemType> _typeMap;

    ObjectType(Map<ObjectKey, ItemType> typeMap) {
        super(ItemTypes.OBJECT);
        this._typeMap = typeMap;
    }

    public Map<ObjectKey, ItemType> getTypeMap() {
        return this._typeMap;
    }

    @Override
    public boolean isObjectType() {
        return true;
    }
}
