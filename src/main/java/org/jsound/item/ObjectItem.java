package org.jsound.item;

import jsound.exceptions.UnexpectedTypeException;
import org.jsound.api.Item;
import org.jsound.api.ItemType;
import org.jsound.type.ObjectKey;
import org.jsound.type.ObjectType;
import org.tyson.TYSONObject;
import org.tyson.TYSONValue;
import org.tyson.TysonItem;

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
        try {
            typeMap = this.getObjectType(itemType).getTypeMap();
        } catch (UnexpectedTypeException e) {
            return false;
        }
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

    @Override
    public TysonItem annotate(ItemType itemType) {
        ObjectType objectType = this.getObjectType(itemType);
        TYSONObject object = new TYSONObject(itemType.getTypeName());
        Map<ObjectKey, ItemType> typeMap = objectType.getTypeMap();
        for (ObjectKey key : typeMap.keySet()) {
            if (_itemMap.containsKey(key.getKeyName())) {
                object.put(key.getKeyName(), _itemMap.get(key.getKeyName()).annotate(typeMap.get(key)));
            } else if (typeMap.get(key).getDefaultValue() != null) {
                object.put(
                    key.getKeyName(),
                    new TYSONValue(
                            typeMap.get(key).getTypeName(),
                            typeMap.get(key).getDefaultValueStringAnnotation()
                    )
                );
            }
        }
        return object;
    }

    private ObjectType getObjectType(ItemType itemType) {
        if (itemType.getItemType().isObjectType()) {
            return (ObjectType) itemType.getItemType();
        }
        throw new UnexpectedTypeException("The object does not have a corresponding schema object");
    }

    public int hashCode() {
        int result = _itemMap.size();
        for (String key : _itemMap.keySet()) {
            result += +_itemMap.get(key).hashCode();
        }
        return result;
    }
}
