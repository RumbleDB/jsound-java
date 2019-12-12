package org.jsound.item;

import jsound.exceptions.UnexpectedTypeException;
import org.jsound.type.FieldDescriptor;
import org.jsound.type.ObjectTypeDescriptor;
import org.jsound.type.TypeDescriptor;
import org.jsound.type.UnionTypeDescriptor;
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
            return ((UnionTypeDescriptor) typeDescriptor).validate(this);
        Map<String, FieldDescriptor> fields;
        try {
            fields = this.getObjectType(typeDescriptor).getFacets().objectContent;
        } catch (UnexpectedTypeException e) {
            return false;
        }
        for (String fieldName : fields.keySet()) {
            if (_itemMap.containsKey(fieldName)) {
                if (!_itemMap.get(fieldName).isValidAgainst(fields.get(fieldName).getType().getTypeDescriptor()))
                    return false;
            } else if (fields.get(fieldName).isRequired() && fields.get(fieldName).getDefaultValue() == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public TysonItem annotateWith(TypeDescriptor typeDescriptor) {
        if (typeDescriptor.isUnionType())
            return ((UnionTypeDescriptor) typeDescriptor).annotate(this);
        ObjectTypeDescriptor objectType = this.getObjectType(typeDescriptor);
        TYSONObject object = new TYSONObject(typeDescriptor.getName());
        Map<String, FieldDescriptor> fields = objectType.getFacets().objectContent;
        for (String fieldName : fields.keySet()) {
            FieldDescriptor fieldDescriptor = fields.get(fieldName);
            if (_itemMap.containsKey(fieldName)) {
                object.put(
                    fieldName,
                    _itemMap.get(fieldName).annotateWith(fieldDescriptor.getType().getTypeDescriptor())
                );
            } else if (fieldDescriptor.getDefaultValue() != null) {
                object.put(
                    fieldName,
                    new TYSONValue(
                            fieldDescriptor.getName(),
                            fieldDescriptor.getDefaultValueAnnotation()
                    )
                );
            }
        }
        for (String key : _itemMap.keySet()) {
            if (!fields.containsKey(key)) {
                object.put(key, new TYSONValue(null, _itemMap.get(key).getStringAnnotation()));
            }
        }
        return object;
    }

    private ObjectTypeDescriptor getObjectType(TypeDescriptor typeDescriptor) {
        TypeDescriptor baseType = typeDescriptor.getBaseType();
        if (baseType.isObjectType()) {
            return (ObjectTypeDescriptor) baseType;
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
