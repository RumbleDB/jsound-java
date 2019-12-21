package org.jsound.type;

import jsound.exceptions.ClosedNotRespectedException;
import jsound.exceptions.ClosedSetBackToFalseException;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.LessRestrictiveFacetException;
import jsound.exceptions.RequiredSertBackToFalseException;
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
    private boolean closedIsChecked = false;

    public ObjectTypeDescriptor(String name, ObjectFacets facets) {
        super(ItemTypes.OBJECT, name);
        this.baseType = null;
        this.facets = facets;
        this.subtypeIsValid = true;
        this.hasResolvedAllFacets = true;
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
    public boolean validate(Item item, boolean isEnumerationItem) {
        if (!item.isObject())
            return false;
        ObjectItem objectItem = (ObjectItem) item;
        for (FacetTypes facetType : this.getFacets().getDefinedFacets()) {
            switch (facetType) {
                case CONTENT:
                    if (!validateContentFacet(objectItem))
                        return false;
                    break;
                case CLOSED:
                    if (!validateClosedFacet(objectItem))
                        return false;
                    break;
                case ENUMERATION:
                    if (!validateEnumeration(objectItem, isEnumerationItem))
                        return false;
                    break;
                default:
                    break;
            }
        }
        return recursivelyValidate(item);
    }

    private boolean validateClosedFacet(ObjectItem objectItem) {
        if (this.getFacets().isClosed()) {
            for (String key : objectItem.getItemMap().keySet()) {
                if (!this.getFacets().getObjectContent().containsKey(key)) {
                    throw new ClosedNotRespectedException(
                            "Type "
                                + this.getName()
                                + " is closed, and the \"content\" facet does not allow for field "
                                + key
                                + "."
                    );
                }
            }
        }
        return true;
    }

    private boolean validateContentFacet(ObjectItem objectItem) {
        for (String fieldName : this.getFacets().getObjectContent().keySet()) {
            FieldDescriptor fieldDescriptor = this.getFacets().getObjectContent().get(fieldName);
            if (objectItem.getItemMap().containsKey(fieldName)) {
                if (
                    !fieldDescriptor
                        .getTypeOrReference()
                        .getTypeDescriptor()
                        .validate(objectItem.getItemMap().get(fieldName), false)
                )
                    return false;
            } else if (fieldDescriptor.isRequired() && fieldDescriptor.getDefaultValue() == null)
                return false;
        }
        return true;
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
                    fieldDescriptor.getTypeOrReference()
                        .getTypeDescriptor()
                        .annotate(objectItem.getItemMap().get(fieldName))
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
                if (this.baseType != null)
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

    @Override
    public void checkBaseType() {
        if (this.subtypeIsValid)
            return;
        ObjectTypeDescriptor baseTypeDescriptor = (ObjectTypeDescriptor) this.baseType.getTypeDescriptor();
        if (!baseTypeDescriptor.isObjectType())
            throw new LessRestrictiveFacetException(
                    "Type "
                        + this.getName()
                        + " is not subtype of "
                        + baseTypeDescriptor
                            .getName()
            );
        for (FacetTypes facetType : this.getFacets().getDefinedFacets()) {
            switch (facetType) {
                case CONTENT:
                    isObjectContentMoreRestrictive(baseTypeDescriptor);
                    break;
                case ENUMERATION:
                    isEnumerationMoreRestrictive(baseTypeDescriptor.facets);
                    break;
            }
        }
        this.subtypeIsValid = true;
        baseTypeDescriptor.checkBaseType();
    }

    private void isObjectContentMoreRestrictive(ObjectTypeDescriptor baseTypeDescriptor) {
        validateDefaultValues();
        if (!baseTypeDescriptor.getFacets().getDefinedFacets().contains(CONTENT))
            return;
        for (FieldDescriptor fieldDescriptor : this.getFacets().getObjectContent().values()) {
            if (baseTypeDescriptor.getFacets().getObjectContent().containsKey(fieldDescriptor.getName()))
                fieldDescriptor.isMoreRestrictive(baseTypeDescriptor);
        }
    }

    @Override
    public void resolveAllFacets() {
        if (this.hasResolvedAllFacets)
            return;
        ObjectTypeDescriptor baseTypeDescriptor = (ObjectTypeDescriptor) this.baseType.getTypeDescriptor();
        if (!this.hasCompatibleType(baseTypeDescriptor))
            throw new LessRestrictiveFacetException(
                    "Type "
                        + this.getName()
                        + " is not subtype of "
                        + baseTypeDescriptor
                            .getName()
            );
        baseTypeDescriptor.resolveAllFacets();
        resolveObjectFacets(baseTypeDescriptor);
        this.hasResolvedAllFacets = true;
    }

    private void resolveObjectFacets(ObjectTypeDescriptor baseTypeDescriptor) {
        for (FacetTypes facetTypes : baseTypeDescriptor.getFacets().getDefinedFacets()) {
            if (!this.getFacets().getDefinedFacets().contains(facetTypes)) {
                switch (facetTypes) {
                    case CLOSED:
                        if (
                            this.getFacets().closedIsSet
                                && !this.getFacets().isClosed()
                                && baseTypeDescriptor.getFacets().isClosed()
                        )
                            throw new ClosedSetBackToFalseException(
                                    "The \"closed\" facet for type "
                                        + this.getName()
                                        + " cannot be set back to false since it was set to true in its baseType "
                                        + this.baseType.getTypeDescriptor().getName()
                                        + "."
                            );
                        this.getFacets().setClosed(baseTypeDescriptor.getFacets().isClosed());
                        break;
                    case CONTENT:
                        this.inheritBaseTypeContent(baseTypeDescriptor);
                        break;
                    case ENUMERATION:
                    case METADATA:
                    case CONSTRAINTS:
                        resolveCommonFacets(baseTypeDescriptor, facetTypes);
                        break;
                }
            }
        }
    }

    private void inheritBaseTypeContent(ObjectTypeDescriptor baseTypeDescriptor) {
        for (FieldDescriptor fieldDescriptor : baseTypeDescriptor.getFacets().getObjectContent().values()) {
            if (!this.getFacets().getObjectContent().containsKey(fieldDescriptor.getName()))
                this.getFacets().getObjectContent().put(fieldDescriptor.name, fieldDescriptor);
            else if (
                fieldDescriptor.isRequired()
                    && !this.getFacets().getObjectContent().get(fieldDescriptor.getName()).isRequired()
            )
                throw new RequiredSertBackToFalseException(
                        "Field "
                            + this.getName()
                            + " cannot be set back to false. It is set to true in baseType "
                            + baseTypeDescriptor.getName()
                            + "."
                );

        }
    }

    @Override
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isObjectType();
    }

    private void validateDefaultValues() {
        for (FieldDescriptor fieldDescriptor : this.getFacets().getObjectContent().values()) {
            if (fieldDescriptor.getDefaultValue() != null) {
                if (
                    !fieldDescriptor.getTypeOrReference()
                        .getTypeDescriptor()
                        .validate(fieldDescriptor.getDefaultValue(), false)
                )
                    throw new InvalidSchemaException(
                            "The default value for field "
                                + this.getName()
                                + " is not valid against its type."
                    );
            }
        }
    }
}
