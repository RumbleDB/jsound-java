package org.jsound.facets;

import com.jsoniter.ValueType;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.UnexpectedTypeException;
import org.jsound.item.Item;
import org.jsound.type.ArrayContentDescriptor;
import org.jsound.type.FieldDescriptor;
import org.jsound.type.UnionContentDescriptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.jsound.cli.JSoundExecutor.object;
import static org.jsound.json.InstanceFileJsonParser.getItemFromObject;

public class Facets {

    public Item metadata = null;
    public List<Item> enumeration = null;
    public List<String> constraints = null;

    public Set<FacetTypes> definedFacets = new HashSet<>();

    public void setFacet(FacetTypes facetType, String typeName) throws IOException {
        checkField(facetType);
        switch (facetType) {
            case ENUMERATION:
                this.enumeration = getEnumerationFromObject();
                break;
            case METADATA:
                this.metadata = getItemFromObject(object);
                break;
            case CONSTRAINTS:
                this.constraints = getConstraintsTypeFromObject();
                break;
        }
        definedFacets.add(facetType);
    }

    public Set<FacetTypes> getDefinedFacets() {
        return definedFacets;
    }

    public List<Item> getEnumeration() {
        return enumeration;
    }

    public void checkField(FacetTypes facetType) {
        if (this.getDefinedFacets().contains(facetType))
            throw new InvalidSchemaException("Field " + facetType.getTypeName() + " is already defined");
    }

    public static String getStringFromObject(String key) throws IOException {
        if (!object.whatIsNext().equals(ValueType.STRING))
            throw new UnexpectedTypeException(
                    key + " should be a string; " + object.whatIsNext().name().toLowerCase() + " was provided instead."
            );
        return object.readString();
    }

    private static List<Item> getEnumerationFromObject() throws IOException {
        if (!object.whatIsNext().equals(ValueType.ARRAY))
            throw new UnexpectedTypeException("Enumeration should be an array.");
        List<Item> enumerationItemTypes = new ArrayList<>();
        while (object.readArray()) {
            enumerationItemTypes.add(getItemFromObject(object));
        }
        return enumerationItemTypes;
    }

    private static List<String> getConstraintsTypeFromObject() throws IOException {
        if (!object.whatIsNext().equals(ValueType.ARRAY))
            throw new UnexpectedTypeException("Constraints should be an array.");
        List<String> constraints = new ArrayList<>();
        while (object.readArray()) {
            constraints.add(getStringFromObject("Each constraint"));
        }
        return constraints;
    }

    public boolean isClosed() {
        return false;
    }

    public Map<String, FieldDescriptor> getObjectContent() {
        throw new UnexpectedTypeException("This type does not have an object content.");
    }

    public ArrayContentDescriptor getArrayContent() {
        throw new UnexpectedTypeException("This type does not have an array content.");
    }

    public UnionContentDescriptor getUnionContent() {
        throw new UnexpectedTypeException("This type does not have a union content.");
    }
}
