package org.jsound.type;

import jsound.exceptions.LessRestrictiveFacetException;
import org.jsound.facets.FacetTypes;
import org.jsound.facets.Facets;
import org.jsound.item.Item;
import org.tyson.TysonItem;

import java.util.Set;

public abstract class TypeDescriptor {
    private ItemTypes type;
    private String name;
    public TypeOrReference baseType;

    TypeDescriptor(ItemTypes type, String name) {
        this.type = type;
        this.name = name;
    }

    TypeDescriptor(ItemTypes type, String name, TypeOrReference baseType) {
        this(type, name);
        this.baseType = baseType;
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

    public abstract boolean validate(Item item);

    public abstract TysonItem annotate(Item item);

    public boolean recursivelyValidate(Item item) {
        if (this.baseType == null)
            return true;
        if (!this.baseType.getTypeDescriptor().validate(item))
            throw new LessRestrictiveFacetException(
                    "Facet for type "
                        + this.getName()
                        + " is less restrictive than that of its baseType "
                        + this.baseType.getTypeDescriptor().getName()
                        + "."
            );
        return true;
    }
}
