package jsound.typedescriptors.object;

import jsound.exceptions.ClosedNotRespectedException;
import jsound.exceptions.ClosedSetBackToFalseException;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.LessRestrictiveFacetException;
import jsound.exceptions.RequiredSertBackToFalseException;
import jsound.facets.FacetTypes;
import jsound.facets.ObjectFacets;
import jsound.item.ObjectItem;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import jsound.typedescriptors.TypeOrReference;
import jsound.types.ItemTypes;
import jsound.tyson.TYSONObject;
import jsound.tyson.TYSONValue;
import jsound.tyson.TysonItem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static jsound.facets.FacetTypes.CLOSED;
import static jsound.facets.FacetTypes.CONTENT;


public class ObjectTypeDescriptor extends TypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Arrays.asList(CONTENT, CLOSED));
    private final ObjectFacets facets;

    public ObjectTypeDescriptor(String name, ObjectFacets facets) {
        super(ItemTypes.OBJECT, name);
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
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isObjectType();
    }

    @Override
    public ObjectFacets getFacets() {
        return facets;
    }

    @Override
    public boolean validate(ItemWrapper itemWrapper, boolean isEnumValue) {
        if (!itemWrapper.getItem().isObjectItem())
            return false;
        ObjectItem objectItem = (ObjectItem) itemWrapper.getItem();
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
                    if (!validateEnumeration(objectItem, isEnumValue))
                        return false;
                    break;
                default:
                    break;
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

    @Override
    public TysonItem annotate(ItemWrapper itemWrapper) {
        ObjectItem objectItem;
        try {
            objectItem = (ObjectItem) itemWrapper.getItem();
        } catch (ClassCastException e) {
            throw new InvalidSchemaException("Annotation not possible. An object is needed.");
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
            if (!this.getFacets().getObjectContent().containsKey(key))
                object.put(key, new TYSONValue(null, objectItem.getItemMap().get(key).getItem()));
        }
        return object;
    }

    @Override
    public void resolveAllFacets() {
        if (this.hasResolvedAllFacets)
            return;
        ObjectTypeDescriptor objectTypeDescriptor = (ObjectTypeDescriptor) this.baseType.getTypeDescriptor();
        if (!this.hasCompatibleType(objectTypeDescriptor))
            throw new LessRestrictiveFacetException(
                    "Type "
                        + this.getName()
                        + " is not compatible with type "
                        + objectTypeDescriptor.getName()
            );
        objectTypeDescriptor.resolveAllFacets();
        resolveObjectFacets(objectTypeDescriptor);
        this.hasResolvedAllFacets = true;
    }

    private void resolveObjectFacets(ObjectTypeDescriptor typeDescriptor) {
        for (FacetTypes facetTypes : typeDescriptor.getFacets().getDefinedFacets()) {
            if (!this.getFacets().getDefinedFacets().contains(facetTypes)) {
                switch (facetTypes) {
                    case CLOSED:
                        if (
                            this.getFacets().closedIsSet
                                && !this.getFacets().isClosed()
                                && typeDescriptor.getFacets().isClosed()
                        )
                            throw new ClosedSetBackToFalseException(
                                    "The \"closed\" facet for type "
                                        + this.getName()
                                        + " cannot be set back to false since it was set to true in its baseType "
                                        + this.baseType.getTypeDescriptor().getName()
                                        + "."
                            );
                        this.getFacets().setClosed(typeDescriptor.getFacets().isClosed());
                        break;
                    case ENUMERATION:
                    case METADATA:
                    case CONSTRAINTS:
                        resolveCommonFacets(typeDescriptor, facetTypes);
                        break;
                }
            }
        }
        if (typeDescriptor.getFacets().getDefinedFacets().contains(CONTENT))
            this.inheritBaseTypeContent(typeDescriptor);
    }

    private void inheritBaseTypeContent(ObjectTypeDescriptor typeDescriptor) {
        for (FieldDescriptor fieldDescriptor : typeDescriptor.getFacets().getObjectContent().values()) {
            if (!this.getFacets().getObjectContent().containsKey(fieldDescriptor.getName()))
                this.getFacets().getObjectContent().put(fieldDescriptor.name, fieldDescriptor);
            else if (
                fieldDescriptor.isRequired()
                    && this.getFacets().getObjectContent().get(fieldDescriptor.getName()).requiredIsSet()
                    && !this.getFacets().getObjectContent().get(fieldDescriptor.getName()).isRequired()
            )
                throw new RequiredSertBackToFalseException(
                        "Field "
                            + this.getName()
                            + " cannot be set back to false. It is set to true in baseType "
                            + typeDescriptor.getName()
                            + "."
                );

        }
    }

    @Override
    public void checkAgainstTypeDescriptor(TypeDescriptor typeDescriptor) {
        if (this.baseTypeIsChecked)
            return;
        ObjectTypeDescriptor objectTypeDescriptor = (ObjectTypeDescriptor) typeDescriptor;
        if (!objectTypeDescriptor.isObjectType())
            throw new LessRestrictiveFacetException(
                    "Type "
                        + this.getName()
                        + " is not compatible with type "
                        + objectTypeDescriptor.getName()
            );
        for (FacetTypes facetType : this.getFacets().getDefinedFacets()) {
            switch (facetType) {
                case CONTENT:
                    isObjectContentMoreRestrictive(objectTypeDescriptor);
                    break;
                case ENUMERATION:
                    isEnumerationMoreRestrictive(objectTypeDescriptor.facets);
                    break;
            }
        }
        this.baseTypeIsChecked = true;
        objectTypeDescriptor.checkBaseType();
    }

    private void isObjectContentMoreRestrictive(ObjectTypeDescriptor baseTypeDescriptor) {
        if (!baseTypeDescriptor.getFacets().getDefinedFacets().contains(CONTENT))
            return;
        for (FieldDescriptor fieldDescriptor : this.getFacets().getObjectContent().values()) {
            if (baseTypeDescriptor.getFacets().getObjectContent().containsKey(fieldDescriptor.getName()))
                fieldDescriptor.isMoreRestrictive(baseTypeDescriptor);
        }
    }
}
