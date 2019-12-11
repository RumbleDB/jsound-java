package org.jsound.type;

import org.jsound.facets.FacetTypes;
import org.jsound.facets.Facets;

import java.util.Set;

public class TypeDescriptor {
    private ItemTypes type;
    private String name;
    protected TypeDescriptor baseType;
    private String stringBaseType;
    private Facets facets;

    public TypeDescriptor(String name, String stringBaseType) {
        this.name = name;
        this.stringBaseType = stringBaseType;
    }

    TypeDescriptor(ItemTypes type, String name, Facets facets) {
        this.type = type;
        this.name = name;
        this.facets = facets;
    }

    TypeDescriptor(ItemTypes type, String name, TypeDescriptor baseType, Facets facets) {
        this(type, name, facets);
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
        return baseType.equals(this) ? baseType : baseType.getBaseType();
    }

    public Facets getFacets() {
        return facets;
    }

    public Set<FacetTypes> getAllowedFacets() {
        return null;
    }
}
