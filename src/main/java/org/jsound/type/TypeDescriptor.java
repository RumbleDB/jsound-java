package org.jsound.type;

import org.jsound.facets.FacetTypes;
import org.jsound.facets.Facets;

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

    public TypeDescriptor getBaseType() {
        return this.equals(baseType.getTypeDescriptor())
            ? baseType.getTypeDescriptor()
            : baseType.getTypeDescriptor().getBaseType();
    }

    public abstract Facets getFacets();

    public abstract Set<FacetTypes> getAllowedFacets();
}
