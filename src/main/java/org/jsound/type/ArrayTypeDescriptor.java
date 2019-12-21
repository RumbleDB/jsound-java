package org.jsound.type;

import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.LessRestrictiveFacetException;
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
import static org.jsound.facets.FacetTypes.ENUMERATION;
import static org.jsound.facets.FacetTypes.MAX_LENGTH;
import static org.jsound.facets.FacetTypes.MIN_LENGTH;

public class ArrayTypeDescriptor extends TypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Arrays.asList(CONTENT, MIN_LENGTH, MAX_LENGTH));
    private final ArrayFacets facets;

    public ArrayTypeDescriptor(String name, ArrayFacets facets) {
        super(ItemTypes.ARRAY, name);
        this.baseType = null;
        this.facets = facets;
        this.subtypeIsValid = true;
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
    public boolean validate(Item item, boolean isEnumerationItem) {
        if (!item.isArray())
            return false;
        ArrayItem arrayItem = (ArrayItem) item;
        for (FacetTypes facetType : this.getFacets().getDefinedFacets()) {
            switch (facetType) {
                case CONTENT:
                    if (!validateContent(arrayItem))
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
                case ENUMERATION:
                    if (!validateEnumeration(arrayItem, isEnumerationItem))
                        return false;
                    break;
                default:
                    break;
            }
        }
        return recursivelyValidate(item);
    }

    private boolean validateContent(ArrayItem arrayItem) {
        TypeDescriptor arrayItemType = this.getFacets().getArrayContent().getType().getTypeDescriptor();
        for (Item itemInArray : arrayItem.getItems()) {
            if (!arrayItemType.validate(itemInArray, false))
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

    @Override
    public void checkBaseType() {
        if (this.subtypeIsValid)
            return;
        ArrayTypeDescriptor baseTypeDescriptor = (ArrayTypeDescriptor) this.baseType.getTypeDescriptor();
        if (!baseTypeDescriptor.isArrayType())
            throw new LessRestrictiveFacetException("Type "
                    + this.getName()
                    + " is not subtype of "
                    + baseTypeDescriptor.getName());
        for (FacetTypes facetType : this.getFacets().getDefinedFacets()) {
            switch (facetType) {
                case CONTENT:
                    isArrayContentMoreRestrictive(baseTypeDescriptor);
                    break;
                case MIN_LENGTH:
                case MAX_LENGTH:
                    areLengthFacetsMoreRestrictive(baseTypeDescriptor);
                    break;
                case ENUMERATION:
                    isEnumerationMoreRestrictive(baseTypeDescriptor.getFacets());
                    break;
            }
        }

        this.subtypeIsValid = true;
        baseTypeDescriptor.checkBaseType();
    }

    private void areLengthFacetsMoreRestrictive(ArrayTypeDescriptor baseTypeDescriptor) {
        if (this.getFacets().getDefinedFacets().contains(MIN_LENGTH) &&
                baseTypeDescriptor.getFacets().getDefinedFacets().contains(MIN_LENGTH) &&
                this.getFacets().minLength < baseTypeDescriptor.getFacets().minLength)
            throw new InvalidSchemaException("Facet minLength for type "
                    + this.getName()
                    + " is less restrictive than that of its baseType.");
        if (this.getFacets().getDefinedFacets().contains(MAX_LENGTH) &&
                baseTypeDescriptor.getFacets().getDefinedFacets().contains(MAX_LENGTH) &&
                this.getFacets().maxLength > baseTypeDescriptor.getFacets().maxLength)
            throw new InvalidSchemaException("Facet maxLength for type "
                    + this.getName()
                    + " is less restrictive than that of its baseType.");
    }

    private void isArrayContentMoreRestrictive(ArrayTypeDescriptor baseTypeDescriptor) {
        if (!baseTypeDescriptor.getFacets().getDefinedFacets().contains(CONTENT))
            return;
        this.getFacets().getArrayContent().getType().getTypeDescriptor().checkBaseType(
        );
    }

    protected boolean isEnumerationMoreRestrictive(ArrayFacets facets) {
        if (!facets.getDefinedFacets().contains(ENUMERATION))
            return enumerationRestrictsMinLength(facets);
        for (Item item : this.getFacets().getEnumeration()) {
            if (!facets.getEnumeration().contains(item))
                return false;
        }
        return true;
    }

    private boolean enumerationRestrictsMinLength(ArrayFacets facets) {
        if (facets.getDefinedFacets().contains(MIN_LENGTH)) {
            for (Item item : this.getFacets().getEnumeration()) {
                if (item.getItems().size() < (facets.minLength))
                    return false;
            }
        }
        return enumerationRestrictsMaxLength(facets);
    }

    private boolean enumerationRestrictsMaxLength(ArrayFacets facets) {
        if (facets.getDefinedFacets().contains(MAX_LENGTH)) {
            for (Item item : this.getFacets().getEnumeration()) {
                if (item.getItems().size() > facets.maxLength)
                    return false;
            }
        }
        return true;
    }
}
