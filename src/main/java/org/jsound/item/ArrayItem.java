package org.jsound.item;

import jsound.exceptions.UnexpectedTypeException;
import org.jsound.api.Item;
import org.jsound.api.ItemType;
import org.jsound.type.ArrayType;
import org.jsound.type.ObjectKey;
import org.jsound.type.ObjectType;
import org.jsound.type.UnionType;
import org.tyson.TYSONArray;
import org.tyson.TysonItem;

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
        if (itemType.isUnionType())
            return ((UnionType) itemType).validate(this);
        ArrayType arrayType;
        try {
            arrayType = getArrayType(itemType);
        } catch (UnexpectedTypeException e) {
            return false;
        }
        for (Item item : _items) {
            if (!item.isValidAgainst(arrayType.getArrayItemsType().getItemType()))
                return false;
        }
        if (_items.isEmpty() || !_items.get(0).isObject())
            return true;
        return this.isUniqueSatisfied(arrayType);
    }


    @Override
    public TysonItem annotateWith(ItemType itemType) {
        if (itemType.isUnionType())
            return ((UnionType) itemType).annotate(this);
        ItemType arrayItemType = getArrayType(itemType).getArrayItemsType();
        TYSONArray array = new TYSONArray(itemType.getTypeName());
        for (Item item : _items) {
            array.add(item.annotateWith(arrayItemType));
        }
        return array;
    }

    private ArrayType getArrayType(ItemType itemType) {
        if (itemType.getItemType().isArrayType())
            return (ArrayType) itemType.getItemType();
        throw new UnexpectedTypeException("Array item does not have a matching array schema");
    }

    private boolean isUniqueSatisfied(ArrayType arrayType) {
        Map<String, Set<Item>> fieldsValues = new HashMap<>();
        ObjectType objectType;
        if (arrayType.getArrayItemsType().getItemType().isObjectType()) {
            objectType = (ObjectType) arrayType.getArrayItemsType().getItemType();
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


    @Override
    public String getStringAnnotation() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        sb.append('[');
        for (Item item : _items) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(item.getStringAnnotation());
        }
        sb.append(']');
        return sb.toString();
    }


    public int hashCode() {
        int result = _items.size();
        for (Item item : _items) {
            result += item.hashCode();
        }
        return result;
    }
}
