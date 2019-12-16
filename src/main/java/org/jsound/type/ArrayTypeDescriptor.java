package org.jsound.type;

import jsound.exceptions.InvalidSchemaException;
import org.jsound.facets.ArrayFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.item.ArrayItem;
import org.jsound.item.Item;
import org.tyson.TYSONArray;
import org.tyson.TysonItem;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.jsound.facets.FacetTypes.CONTENT;
import static org.jsound.facets.FacetTypes.MAX_LENGTH;
import static org.jsound.facets.FacetTypes.MIN_LENGTH;

public class ArrayTypeDescriptor extends TypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Arrays.asList(CONTENT, MIN_LENGTH, MAX_LENGTH));
    private final ArrayFacets facets;

    public ArrayTypeDescriptor(String name, ArrayFacets facets) {
        super(ItemTypes.ARRAY, name);
        this.baseType = new TypeOrReference(this);
        this.facets = facets;
    }

    public ArrayTypeDescriptor(String name, TypeOrReference baseType, ArrayFacets facets) {
        super(ItemTypes.ARRAY, name, baseType);
        this.facets = facets;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean validate(Item item) {
        if (!item.isArray())
            return false;
        ArrayItem arrayItem;
        try {
            arrayItem = (ArrayItem) item;
        } catch (ClassCastException e) {
            return false;
        }
        for (FacetTypes facetType : this.getFacets().getDefinedFacets()) {
            switch (facetType) {
                case CONTENT:
                    if (!checkContent(arrayItem))
                        return false;
                    break;
                case MIN_LENGTH:
                    if (arrayItem.getItems().size() < this.getFacets().minLength)
                        return false;
                    break;
                case MAX_LENGTH:
                    if (arrayItem.getItems().size() > this.getFacets().maxLength)
                        return false;
                    break;
            }
        }
        return this.baseType.getTypeDescriptor().equals(this) || this.baseType.getTypeDescriptor().validate(item);
    }

    private boolean checkContent(ArrayItem arrayItem) {
        TypeDescriptor arrayItemType = this.getFacets().getArrayContent().getType().getTypeDescriptor();
        for (Item itemInArray : arrayItem.getItems()) {
            if (!arrayItemType.validate(itemInArray))
                return false;
        }

        if (arrayItem.getItems().isEmpty() || !arrayItem.getItems().get(0).isObject())
            return true;
        return this.isUniqueSatisfied(arrayItem.getItems());
    }

    @Override
    public TysonItem annotate(Item item) {
        ArrayItem arrayItem;
        try {
            arrayItem = (ArrayItem) item;
        } catch (ClassCastException e) {
            throw new InvalidSchemaException("Cannot annotate. Need an array item.");
        }
        TypeDescriptor arrayItemType = this.getFacets().getArrayContent().getType().getTypeDescriptor();
        TYSONArray array = new TYSONArray(this.getName());
        for (Item itemInArray : arrayItem.getItems()) {
            array.add(arrayItemType.annotate(itemInArray));
        }
        return array;
    }

    @Override
    public boolean isArrayType() {
        return true;
    }

    @Override
    public ArrayFacets getFacets() {
        return facets;
    }

    private boolean isUniqueSatisfied(List<Item> arrayItems) {
        Map<String, Set<Item>> fieldsValues = new HashMap<>();
        ObjectTypeDescriptor objectType;
        if (this.getFacets().getArrayContent().getType().getTypeDescriptor().isObjectType()) {
            objectType = (ObjectTypeDescriptor) this.getFacets().getArrayContent().getType().getTypeDescriptor();
        } else
            return true;
        Map<String, FieldDescriptor> fields = objectType.getFacets().getObjectContent();
        for (String fieldName : fields.keySet()) {
            if (fields.get(fieldName).isUnique()) {
                for (Item item : arrayItems) {
                    if (item.getItemMap().containsKey(fieldName)) {
                        Item value = item.getItemMap().get(fieldName);
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
        }
        return true;
    }
}
