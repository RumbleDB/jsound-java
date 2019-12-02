package org.jsound.item;

import org.jsound.api.Item;
import org.jsound.api.ItemType;
import org.jsound.type.ArrayType;
import org.jsound.type.ObjectKey;
import org.jsound.type.ObjectType;
import org.jsound.type.UserDefinedType;

import java.util.*;

public class ArrayItem extends Item {

    private List<Item> _items;

    ArrayItem(List<Item> items) {
        super();
        this._items = items;
    }

    public List<Item> getItems() {
        return this._items;
    }

    @Override
    public boolean isValidAgainst(ItemType itemType) {
        ArrayType arrayType;
        if (itemType.isArrayType())
            arrayType = (ArrayType) itemType;
        else if (itemType.isUserDefinedType()
        && ((UserDefinedType) itemType).getType().isArrayType())
            arrayType = (ArrayType) ((UserDefinedType) itemType).getType();
        else
            return false;
        for (Item item : _items) {
            if (!item.isValidAgainst(arrayType.getArrayItemsType()))
                return false;
        }
        if (_items.isEmpty() || !_items.get(0).isObject())
            return true;
        return this.isUniqueSatisfied(arrayType);
    }

    private boolean isUniqueSatisfied(ArrayType arrayType) {
        Map<String, Set<Item>> fieldsValues = new HashMap<>();
        ObjectType objectType;
        if (arrayType.getArrayItemsType().isObjectType()) {
            objectType = (ObjectType) arrayType.getArrayItemsType();
        } else if (
            arrayType.getArrayItemsType().isUserDefinedType()
                &&
                ((UserDefinedType) arrayType.getArrayItemsType()).getType().isObjectType()
        ) {
            objectType = (ObjectType) ((UserDefinedType) arrayType.getArrayItemsType()).getType();
        } else
            return true;
        for (ObjectKey key : objectType.getTypeMap().keySet()) {
            if (key.isUnique()) {
                for (Item item : _items) {
                    Item value = ((ObjectItem) item).getItemMap().get(key.getKeyName());
                    if (fieldsValues.containsKey(key.getKeyName())) {
                        if (fieldsValues.get(key.getKeyName()).contains(value)) {
                            return false;
                        }
                        fieldsValues.get(key.getKeyName()).add(value);
                    } else {
                        fieldsValues.put(key.getKeyName(), new HashSet<>(Collections.singleton(value)));
                    }
                }
            }
        }
        return true;
    }

    public int hashCode() {
        int result = 0;
        result += _items.size();
        for (Item item : _items) {
            result += item.hashCode();
        }
        return result;
    }
}
