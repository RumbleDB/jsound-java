package org.jsound.item;

import jsound.exceptions.UnexpectedTypeException;
import org.jsound.api.Item;
import org.jsound.api.ItemType;
import org.jsound.type.ArrayType;
import org.jsound.type.ObjectKey;
import org.jsound.type.ObjectType;
import org.jsound.type.UserDefinedType;
import org.tyson.TYSONArray;

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
        try {
            arrayType = getArrayType(itemType);
        } catch (UnexpectedTypeException e) {
            return false;
        }
        for (Item item : _items) {
            if (!item.isValidAgainst(arrayType.getArrayItemsType()))
                return false;
        }
        if (_items.isEmpty() || !_items.get(0).isObject())
            return true;
        return this.isUniqueSatisfied(arrayType);
    }


    @Override
    public Object annotate(ItemType itemType) {
        ItemType arrayItemType = getArrayType(itemType).getArrayItemsType();
        TYSONArray array = new TYSONArray(
                itemType.isUserDefinedType() ? ((UserDefinedType) itemType).getName() : itemType.getType().getTypeName()
        );
        for (Item item : _items) {
            array.add(item.annotate(arrayItemType));
        }
        return array;
    }

    private ArrayType getArrayType(ItemType itemType) {
        if (itemType.isArrayType())
            return (ArrayType) itemType;
        else if (
            itemType.isUserDefinedType()
                && ((UserDefinedType) itemType).getItemType().isArrayType()
        )
            return (ArrayType) ((UserDefinedType) itemType).getItemType();
        throw new UnexpectedTypeException("Array item does not have a matching array schema");
    }

    private boolean isUniqueSatisfied(ArrayType arrayType) {
        Map<String, Set<Item>> fieldsValues = new HashMap<>();
        ObjectType objectType;
        if (arrayType.getArrayItemsType().isObjectType()) {
            objectType = (ObjectType) arrayType.getArrayItemsType();
        } else if (
            arrayType.getArrayItemsType().isUserDefinedType()
                &&
                ((UserDefinedType) arrayType.getArrayItemsType()).getItemType().isObjectType()
        ) {
            objectType = (ObjectType) ((UserDefinedType) arrayType.getArrayItemsType()).getItemType();
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
