package org.jsound.api;

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

    public String getName() {
        return name;
    }

    public ItemTypes getType() {
        return type;
    }

    public TypeDescriptor getBaseType() {
        return baseType;
    }

    public Facets getFacets() {
        return facets;
    }

    public Set<FacetTypes> getAllowedFacets() {
        return null;
    }
}
