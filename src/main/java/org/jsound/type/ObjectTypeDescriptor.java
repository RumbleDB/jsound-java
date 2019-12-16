package org.jsound.type;

import jsound.exceptions.InvalidSchemaException;
import org.jsound.facets.FacetTypes;
import org.jsound.facets.ObjectFacets;
import org.jsound.item.Item;
import org.jsound.item.ObjectItem;
import org.tyson.TYSONObject;
import org.tyson.TYSONValue;
import org.tyson.TysonItem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.CLOSED;
import static org.jsound.facets.FacetTypes.CONTENT;


public class ObjectTypeDescriptor extends TypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Arrays.asList(CONTENT, CLOSED));
    private final ObjectFacets facets;

    public ObjectTypeDescriptor(String name, ObjectFacets facets) {
        super(ItemTypes.OBJECT, name);
        this.baseType = new TypeOrReference(this);
        this.facets = facets;
    }

    public ObjectTypeDescriptor(String name, TypeOrReference baseType, ObjectFacets facets) {
        super(ItemTypes.OBJECT, name, baseType);
        this.facets = facets;
    }

    @Override
    public boolean isObjectType() {
        return true;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean validate(Item item) {
        if (!item.isObject())
            return false;
        ObjectItem objectItem;
        try {
            objectItem = (ObjectItem) item;
        } catch (ClassCastException e) {
            return false;
        }
        if (this.getFacets().isClosed()) {
            for (String key : objectItem.getItemMap().keySet()) {
                if (!this.getFacets().getObjectContent().containsKey(key)) {
                    return false;
                }
            }
        }
        for (String fieldName : this.getFacets().getObjectContent().keySet()) {
            FieldDescriptor fieldDescriptor = this.getFacets().getObjectContent().get(fieldName);
            if (objectItem.getItemMap().containsKey(fieldName)) {
                if (
                    !fieldDescriptor
                        .getTypeOrReference()
                        .getTypeDescriptor()
                        .validate(objectItem.getItemMap().get(fieldName))
                )
                    return false;
            } else if (fieldDescriptor.isRequired() && fieldDescriptor.getDefaultValue() == null)
                return false;
            if (fieldDescriptor.getDefaultValue() != null)
                if (!fieldDescriptor.getTypeOrReference().getTypeDescriptor().validate(fieldDescriptor.getDefaultValue()))
                    return false;
        }
        return this.baseType.getTypeDescriptor().equals(this) || this.baseType.getTypeDescriptor().validate(item);
    }

    @Override
    public TysonItem annotate(Item item) {
        ObjectItem objectItem;
        try {
            objectItem = (ObjectItem) item;
        } catch (ClassCastException e) {
            throw new InvalidSchemaException("Annotation not possible. Need an object.");
        }

        TYSONObject object = new TYSONObject(this.getName());
        for (String fieldName : this.getFacets().getObjectContent().keySet()) {
            FieldDescriptor fieldDescriptor = this.getFacets().getObjectContent().get(fieldName);
            if (objectItem.getItemMap().containsKey(fieldName)) {
                object.put(
                    fieldName,
                    fieldDescriptor.getTypeOrReference().getTypeDescriptor().annotate(objectItem.getItemMap().get(fieldName))
                );
            } else if (fieldDescriptor.getDefaultValue() != null) {
                object.put(
                    fieldName,
                    fieldDescriptor.getTypeOrReference().getTypeDescriptor().annotate(fieldDescriptor.getDefaultValue())
                );
            }
        }

        for (String key : objectItem.getItemMap().keySet()) {
            if (!this.getFacets().getObjectContent().containsKey(key)) {
                if (!this.baseType.getTypeDescriptor().equals(this))
                    object.put(key, this.baseType.getTypeDescriptor().annotate(objectItem.getItemMap().get(key)));
                else
                    object.put(key, new TYSONValue(null, objectItem.getItemMap().get(key)));
            }
        }
        return object;
    }

    @Override
    public ObjectFacets getFacets() {
        return facets;
    }
}
