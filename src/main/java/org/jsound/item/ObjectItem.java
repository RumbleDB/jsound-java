package org.jsound.item;

import jsound.exceptions.UnexpectedTypeException;
import org.jsound.api.Item;
import org.jsound.api.ItemType;
import org.jsound.api.TypeDescriptor;
import org.jsound.type.ObjectKey;
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
    public boolean isValidAgainst(TypeDescriptor typeDescriptor) {
        if (typeDescriptor.isUnionType())
            return ((UnionType) typeDescriptor).validate(this);
        Map<ObjectKey, ItemType> typeMap;
        try {
            typeMap = this.getObjectType(typeDescriptor).getTypeMap();
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
    public TysonItem annotateWith(TypeDescriptor typeDescriptor) {
        if (typeDescriptor.isUnionType())
            return ((UnionType) typeDescriptor).annotate(this);
        ObjectType objectType = this.getObjectType(typeDescriptor);
        TYSONObject object = new TYSONObject(typeDescriptor.getTypeName());
        Map<ObjectKey, ItemType> typeMap = objectType.getTypeMap();
        for (ObjectKey key : typeMap.keySet()) {
            if (_itemMap.containsKey(key.getKeyName())) {
                object.put(key.getKeyName(), _itemMap.get(key.getKeyName()).annotateWith(typeMap.get(key)));
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
        for (String key : _itemMap.keySet()) {
            if (!typeMap.containsKey(new ObjectKey(key, false))) {
                object.put(key, new TYSONValue(null, _itemMap.get(key).getStringAnnotation()));
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


    @Override
    public String getStringAnnotation() {
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (String key : _itemMap.keySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append("\"").append(key).append("\"").append(": ").append(_itemMap.get(key).getStringAnnotation());
        }
        sb.append('}');
        return sb.toString();
    }

    public int hashCode() {
        int result = _itemMap.size();
        for (String key : _itemMap.keySet()) {
            result += +_itemMap.get(key).hashCode();
        }
        return result;
    }
}
