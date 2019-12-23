package jsound.typedescriptors.array;

import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.LessRestrictiveFacetException;
import jsound.typedescriptors.object.FieldDescriptor;
import jsound.typedescriptors.object.ObjectTypeDescriptor;
import jsound.facets.ArrayFacets;
import jsound.facets.FacetTypes;
import jsound.item.ArrayItem;
import org.api.Item;
import org.api.TypeDescriptor;
import jsound.typedescriptors.TypeOrReference;
import jsound.types.ItemTypes;
import jsound.tyson.TYSONArray;
import jsound.tyson.TysonItem;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static jsound.facets.FacetTypes.CONTENT;
import static jsound.facets.FacetTypes.MAX_LENGTH;
import static jsound.facets.FacetTypes.MIN_LENGTH;

public class ArrayTypeDescriptor extends TypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(Arrays.asList(CONTENT, MIN_LENGTH, MAX_LENGTH));
    private final ArrayFacets facets;

    public ArrayTypeDescriptor(String name, ArrayFacets facets) {
        super(ItemTypes.ARRAY, name);
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
    public boolean validate(Item item, boolean isEnumValue) {
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
                    if (!validateEnumeration(arrayItem, isEnumValue))
                        return false;
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    private boolean validateContent(ArrayItem arrayItem) {
        TypeDescriptor arrayItemType = this.getFacets().getArrayContent().getType().getTypeDescriptor();
        for (Item itemInArray : arrayItem.getItems()) {
            if (!arrayItemType.validate(itemInArray, false))
                return false;
        }

        return !this.getFacets().getArrayContent().getType().getTypeDescriptor().isObjectType()
            || this.isUniqueSatisfied(arrayItem.getItems());
    }

    @Override
    public TysonItem annotate(Item item) {
        ArrayItem arrayItem;
        try {
            arrayItem = (ArrayItem) item;
        } catch (ClassCastException e) {
            throw new InvalidSchemaException("Cannot annotate. An array is needed.");
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

    @Override
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isArrayType();
    }

    private boolean isUniqueSatisfied(List<Item> arrayItems) {
        Map<String, Set<Item>> fieldsValues = new HashMap<>();
        ObjectTypeDescriptor objectType = (ObjectTypeDescriptor) this.getFacets()
            .getArrayContent()
            .getType()
            .getTypeDescriptor();
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
    public void resolveAllFacets() {
        if (this.hasResolvedAllFacets)
            return;
        ArrayTypeDescriptor baseTypeDescriptor = (ArrayTypeDescriptor) this.baseType.getTypeDescriptor();
        if (!this.hasCompatibleType(baseTypeDescriptor))
            throw new LessRestrictiveFacetException(
                    "Type "
                        + this.getName()
                        + " is not subtype of "
                        + baseTypeDescriptor
                            .getName()
            );
        baseTypeDescriptor.resolveAllFacets();
        resolveArrayFacets(baseTypeDescriptor);
        this.hasResolvedAllFacets = true;
    }

    private void resolveArrayFacets(ArrayTypeDescriptor baseTypeDescriptor) {
        for (FacetTypes facetTypes : baseTypeDescriptor.getFacets().getDefinedFacets()) {
            if (!this.getFacets().getDefinedFacets().contains(facetTypes)) {
                switch (facetTypes) {
                    case MIN_LENGTH:
                        this.getFacets().minLength = baseTypeDescriptor.getFacets().minLength;
                        break;
                    case MAX_LENGTH:
                        this.getFacets().maxLength = baseTypeDescriptor.getFacets().maxLength;
                        break;
                    case CONTENT:
                        this.getFacets().arrayContent = baseTypeDescriptor.getFacets().arrayContent;
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

    @Override
    public void checkAgainstTypeDescriptor(TypeDescriptor typeDescriptor) {
        if (this.baseTypeIsChecked)
            return;
        ArrayTypeDescriptor arrayTypeDescriptor = (ArrayTypeDescriptor) typeDescriptor;
        for (FacetTypes facetType : this.getFacets().getDefinedFacets()) {
            switch (facetType) {
                case CONTENT:
                    isArrayContentMoreRestrictive(arrayTypeDescriptor);
                    break;
                case MIN_LENGTH:
                case MAX_LENGTH:
                    areLengthFacetsMoreRestrictive(arrayTypeDescriptor);
                    break;
                case ENUMERATION:
                    isEnumerationMoreRestrictive(arrayTypeDescriptor.getFacets());
                    break;
            }
        }

        this.baseTypeIsChecked = true;
        arrayTypeDescriptor.checkBaseType();
    }

    private void isArrayContentMoreRestrictive(ArrayTypeDescriptor typeDescriptor) {
        if (!typeDescriptor.getFacets().getDefinedFacets().contains(CONTENT))
            return;
        this.getFacets()
            .getArrayContent()
            .getType()
            .getTypeDescriptor()
            .checkAgainstTypeDescriptor(
                typeDescriptor.getFacets().getArrayContent().getType().getTypeDescriptor()
            );
    }

    private void areLengthFacetsMoreRestrictive(ArrayTypeDescriptor baseTypeDescriptor) {
        if (
            this.getFacets().getDefinedFacets().contains(MIN_LENGTH)
                && baseTypeDescriptor.getFacets().getDefinedFacets().contains(MIN_LENGTH)
                && this.getFacets().minLength < baseTypeDescriptor.getFacets().minLength
        )
            throw new InvalidSchemaException(
                    "Facet minLength for type "
                        + this.getName()
                        + " is less restrictive than that of its baseType."
            );
        if (
            this.getFacets().getDefinedFacets().contains(MAX_LENGTH)
                && baseTypeDescriptor.getFacets().getDefinedFacets().contains(MAX_LENGTH)
                && this.getFacets().maxLength > baseTypeDescriptor.getFacets().maxLength
        )
            throw new InvalidSchemaException(
                    "Facet maxLength for type "
                        + this.getName()
                        + " is less restrictive than that of its baseType."
            );
    }
}
