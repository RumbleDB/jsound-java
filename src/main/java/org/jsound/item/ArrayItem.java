package org.jsound.item;

import jsound.exceptions.UnexpectedTypeException;
import org.jsound.type.ArrayTypeDescriptor;
import org.jsound.type.FieldDescriptor;
import org.jsound.type.ObjectTypeDescriptor;
import org.jsound.type.TypeDescriptor;
import org.jsound.type.UnionTypeDescriptor;
import org.tyson.TYSONArray;
import org.tyson.TysonItem;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ArrayItem extends Item {

    private List<Item> _items;

    ArrayItem(List<Item> items) {
        super();
        this._items = items;
    }

    @Override
    public boolean isValidAgainst(TypeDescriptor typeDescriptor) {
        if (typeDescriptor.isUnionType())
            return ((UnionTypeDescriptor) typeDescriptor).validate(this);
        TypeDescriptor arrayItemType;
        try {
            arrayItemType = getArrayType(typeDescriptor).getFacets().arrayContent.getType().getTypeDescriptor();
        } catch (UnexpectedTypeException e) {
            return false;
        }
        for (Item item : _items) {
            if (!item.isValidAgainst(arrayItemType))
                return false;
        }
        if (_items.isEmpty() || !_items.get(0).isObject())
            return true;
        return this.isUniqueSatisfied(arrayItemType);
    }


    @Override
    public TysonItem annotateWith(TypeDescriptor typeDescriptor) {
        if (typeDescriptor.isUnionType())
            return ((UnionTypeDescriptor) typeDescriptor).annotate(this);
        TypeDescriptor arrayItemType = getArrayType(typeDescriptor).getFacets().arrayContent.getType()
            .getTypeDescriptor();
        TYSONArray array = new TYSONArray(typeDescriptor.getName());
        for (Item item : _items) {
            array.add(item.annotateWith(arrayItemType));
        }
        return array;
    }

    private ArrayTypeDescriptor getArrayType(TypeDescriptor typeDescriptor) {
        TypeDescriptor baseType = typeDescriptor.getBaseType();
        if (baseType.isArrayType())
            return (ArrayTypeDescriptor) baseType;
        throw new UnexpectedTypeException("Array atomicItems does not have a matching array schema");
    }

    private boolean isUniqueSatisfied(TypeDescriptor arrayItemType) {
        Map<String, Set<Item>> fieldsValues = new HashMap<>();
        ObjectTypeDescriptor objectType;
        if (arrayItemType.isObjectType()) {
            objectType = (ObjectTypeDescriptor) arrayItemType;
        } else
            return true;
        Map<String, FieldDescriptor> fields = objectType.getFacets().objectContent;
        for (String fieldName : fields.keySet()) {
            if (fields.get(fieldName).isUnique()) {
                for (Item item : _items) {
                    Item value = ((ObjectItem) item).getItemMap().get(fieldName);
                    if (fieldsValues.containsKey(fieldName)) {
                        if (fieldsValues.get(fieldName).contains(value)) {
                            return false;
                        }
                        fieldsValues.get(fieldName).add(value);
                    } else {
                        fieldsValues.put(fieldName, new HashSet<>(Collections.singleton(value)));
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
