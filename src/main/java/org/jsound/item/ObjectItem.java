package org.jsound.item;

import org.jsound.api.Item;
import org.jsound.api.ItemType;
import org.jsound.type.ObjectKey;
import org.jsound.type.ObjectType;
import org.jsound.type.UserDefinedType;

import java.util.Map;

public class ObjectItem extends Item {
    private Map<String, Item> _itemMap;

    ObjectItem(Map<String, Item> itemMap) {
        this._itemMap = itemMap;
    }

    Map<String, Item> getItemMap() {
        return this._itemMap;
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public boolean isValidAgainst(ItemType itemType) {
        Map<ObjectKey, ItemType> typeMap;
        if (itemType.isObjectType()) {
            typeMap = ((ObjectType) itemType).getTypeMap();
        } else if (
                itemType.isUserDefinedType()
                        &&
                        ((UserDefinedType) itemType).getType().isObjectType()) {
            typeMap = ((ObjectType) ((UserDefinedType) itemType).getType()).getTypeMap();
        }
        else
            return false;
        for (ObjectKey key : typeMap.keySet()) {
            if (_itemMap.containsKey(key.getKeyName())) {
                if (
                    (_itemMap.get(key.getKeyName()).isNull() && !key.allowsNull())
                        ||
                        (!_itemMap.get(key.getKeyName()).isValidAgainst(typeMap.get(key)))
                ) {
                    return false;
                }
            } else if (key.isRequired() && typeMap.get(key).getDefaultValue() == null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int result = 0;
        result += _itemMap.size();
        for (String key : _itemMap.keySet()) {
            result += +_itemMap.get(key).hashCode();
        }
        return result;
    }
}
