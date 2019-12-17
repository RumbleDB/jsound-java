package org.jsound.type;

import jsound.exceptions.ClosedNotRespectedException;
import jsound.exceptions.ClosedSetBackToFalseException;
import jsound.exceptions.InvalidSchemaException;
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
                    checkClosedFacet();
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

//    private void checkClosedFacetCorrectness(boolean closedIsSetToFalse) {
//        if (!this.closedIsChecked && closedIsSetToFalse) {
//            if (this.baseType != null) {
//                if (this.baseType.getTypeDescriptor().getFacets().isClosed())
//                    throw new ClosedSetBackToFalseException(
//                            "The \"closed\" facet for type "
//                                + this.getName()
//                                + " cannot be set back to false since it was set to true in its baseType "
//                                + this.baseType.getTypeDescriptor().getName()
//                                + "."
//                    );
//                ((ObjectTypeDescriptor) this.baseType.getTypeDescriptor()).checkClosedFacetCorrectness(closedIsSetToFalse);
//            }
//        }
//        this.closedIsChecked = true;
//    }

    private boolean checkClosedFacet() {
        boolean objectDescriptorIsClosed;
        if (baseType == null || this.getFacets().isClosed() || this.closedIsChecked) {
            objectDescriptorIsClosed = this.getFacets().isClosed();
        } else {
            objectDescriptorIsClosed = ((ObjectTypeDescriptor) this.baseType.getTypeDescriptor()).checkClosedFacet(); //THERE IS A BASETYPE THAT SET CLOSED TO TRUE
            if (objectDescriptorIsClosed) {
                if (this.getFacets().getDefinedFacets().contains(CLOSED)) //CLOSED WAS EXPLICITLY SET BACK TO FALSE
                    throw new ClosedSetBackToFalseException(
                            "The \"closed\" facet for type "
                                    + this.getName()
                                    + " cannot be set back to false since it was set to true in its baseType "
                                    + this.baseType.getTypeDescriptor().getName()
                                    + "."
                    );
                else
                    this.getFacets().setClosed(true);
            }
        }
        this.closedIsChecked = true;
        return objectDescriptorIsClosed;
    }

    private boolean validateClosedFacet(ObjectItem objectItem) {
        if (this.getFacets().isClosed()) {
            for (String key : objectItem.getItemMap().keySet()) {
                if (!this.getFacets().getObjectContent().containsKey(key)) {
                    throw new ClosedNotRespectedException("Type " + this.getName() +
                            " is closed, and the \"content\" facet does not allow for field " + key + ".");
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
            else if (!fieldDescriptor.isRequired())
                checkRequiredField(fieldDescriptor);
            if (!validateDefaultValue(fieldDescriptor))
                return false;
        }
        return true;
    }

    private void checkRequiredField(FieldDescriptor fieldDescriptor) {
        if (!fieldDescriptor.requiredIsChecked) {
            if (this.baseType != null) {
                if (this.baseType.getTypeDescriptor().getFacets().getObjectContent().containsKey(fieldDescriptor.getName())) {
                    if (this.baseType.getTypeDescriptor().getFacets().getObjectContent().get(fieldDescriptor.getName()).isRequired())
                        throw new RequiredSertBackToFalseException("Field " + fieldDescriptor.getName() + " for type " +
                                this.getName() + " cannot be set back to false. It is set to true in baseType " + this.baseType.getTypeDescriptor().getName() + ".");
                }
                ((ObjectTypeDescriptor) this.baseType.getTypeDescriptor()).checkRequiredField(fieldDescriptor);
            }
        }
        fieldDescriptor.requiredIsChecked = true;
    }

    private boolean validateDefaultValue(FieldDescriptor fieldDescriptor) {
        if (fieldDescriptor.getDefaultValue() != null) {
            return fieldDescriptor.getTypeOrReference()
                .getTypeDescriptor()
                .validate(fieldDescriptor.getDefaultValue(), false);
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
}
