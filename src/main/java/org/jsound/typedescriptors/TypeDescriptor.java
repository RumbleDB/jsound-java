package org.jsound.typedescriptors;

import jsound.exceptions.InvalidEnumValueException;
import org.jsound.facets.FacetTypes;
import org.jsound.facets.Facets;
import org.jsound.item.Item;
import org.jsound.types.ItemTypes;
import org.tyson.TYSONValue;
import org.tyson.TysonItem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.ENUMERATION;

public abstract class TypeDescriptor {
    private ItemTypes type;
    private String name;
    public TypeOrReference baseType;
    private boolean enumerationIsValid = false;
    protected boolean baseTypeIsChecked = false;
    protected boolean hasResolvedAllFacets = false;
    private static TypeDescriptor valueInstance;

    public TypeDescriptor(ItemTypes type, String name) {
        this.type = type;
        this.name = name;
        this.baseType = null;
        this.baseTypeIsChecked = true;
        this.hasResolvedAllFacets = true;
    }

    public TypeDescriptor(ItemTypes type, String name, TypeOrReference baseType) {
        this.type = type;
        this.name = name;
        this.baseType = baseType;
    }

    private TypeDescriptor() {
        this.type = ItemTypes.VALUE;
    }

    public static TypeDescriptor getValueInstance() {
        if (valueInstance == null) {
            valueInstance = new TypeDescriptor() {
                @Override
                public Facets getFacets() {
                    return new Facets();
                }

                @Override
                public Set<FacetTypes> getAllowedFacets() {
                    return new HashSet<>(Arrays.asList(FacetTypes.values()));
                }

                @Override
                public boolean validate(Item item, boolean isEnumValue) {
                    return true;
                }

                @Override
                public TysonItem annotate(Item item) {
                    return new TYSONValue(null, item);
                }

                @Override
                protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
                    return true;
                }
            };
        }
        return valueInstance;
    }

    public boolean isAtomicType() {
        return false;
    }

    public boolean isObjectType() {
        return false;
    }

    public boolean isArrayType() {
        return false;
    }

    public boolean isUnionType() {
        return false;
    }

    public boolean isStringType() {
        return false;
    }

    public boolean isIntegerType() {
        return false;
    }

    public boolean isDecimalType() {
        return false;
    }

    public boolean isDoubleType() {
        return false;
    }

    public boolean isBooleanType() {
        return false;
    }

    public boolean isDateTimeType() {
        return false;
    }

    public boolean isDateType() {
        return false;
    }

    public boolean isTimeType() {
        return false;
    }

    public boolean isDurationType() {
        return false;
    }

    public boolean isYearMonthDurationType() {
        return false;
    }

    public boolean isDayTimeDurationType() {
        return false;
    }

    public boolean isHexBinaryType() {
        return false;
    }

    public boolean isBase64BinaryType() {
        return false;
    }

    public boolean isNullType() {
        return false;
    }

    public boolean isAnyURIType() {
        return false;
    }

    public String getName() {
        return name;
    }

    public ItemTypes getType() {
        return type;
    }

    public void setType(ItemTypes type) {
        this.type = type;
    }

    public TypeDescriptor getRootBaseType() {
        return this.baseType == null
            ? this
            : baseType.getTypeDescriptor().getRootBaseType();
    }

    public abstract Facets getFacets();

    public abstract Set<FacetTypes> getAllowedFacets();

    public abstract boolean validate(Item item, boolean isEnumValue);

    public abstract TysonItem annotate(Item item);

    protected boolean isEnumerationMoreRestrictive(Facets facets) {
        validateEnumerationValues();
        if (facets.getDefinedFacets().contains(ENUMERATION)) {
            for (Item item : this.getFacets().getEnumeration()) {
                if (!facets.getEnumeration().contains(item))
                    return false;
            }
        }
        return true;
    }

    private void validateEnumerationValues() {
        if (this.enumerationIsValid)
            return;
        for (Item enumItem : this.getFacets().getEnumeration()) {
            if (!this.validate(enumItem, true)) {
                throw new InvalidEnumValueException(
                        "Value "
                            + enumItem.getStringValue()
                            + " in enumeration is not in the type value space for type "
                            + this.getName()
                            + "."
                );
            }
        }
        this.enumerationIsValid = true;
    }

    protected boolean validateEnumeration(Item item, boolean isEnumerationItem) {
        if (isEnumerationItem)
            return true;
        try {
            return validateItemAgainstEnumeration(item);
        } catch (Exception e) {
            throw new InvalidEnumValueException(
                    "A value in enumeration is not in the type value space for type " + this.getName() + "."
            );
        }
    }

    protected boolean validateItemAgainstEnumeration(Item item) throws Exception {
        for (Item enumItem : this.getFacets().getEnumeration()) {
            if (item.equals(enumItem))
                return true;
        }
        return false;
    }

    public void resolveAllFacets() {}

    public void resolveCommonFacets(TypeDescriptor typeDescriptor, FacetTypes facetType) {
        switch (facetType) {
            case ENUMERATION:
                this.getFacets().enumeration = typeDescriptor.getFacets().enumeration;
                break;
            case METADATA:
                this.getFacets().metadata = typeDescriptor.getFacets().metadata;
                break;
            case CONSTRAINTS:
                this.getFacets().constraints = typeDescriptor.getFacets().constraints;
                break;
        }
    }

    public void checkAgainstTypeDescriptor(TypeDescriptor typeDescriptor) {}

    public void checkBaseType() {
        if (this.baseType != null)
            checkAgainstTypeDescriptor(this.baseType.getTypeDescriptor());
    }

    protected abstract boolean hasCompatibleType(TypeDescriptor typeDescriptor);
}
