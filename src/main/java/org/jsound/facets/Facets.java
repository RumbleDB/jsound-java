package org.jsound.facets;

import com.jsoniter.ValueType;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.UnexpectedTypeException;
import org.jsound.item.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.jsound.cli.JSoundExecutor.object;
import static org.jsound.json.InstanceFileJsonParser.getItemFromObject;

public class Facets {

    Item metadata = null;
    private List<Item> enumeration = null;
    List<String> constraints = null;
    private Boolean hasEnumeration = false;

    protected Set<FacetTypes> definedFacets = new HashSet<>();

    public void setFacet(FacetTypes facetType) throws IOException {
        definedFacets.add(facetType);
        switch (facetType) {
            case ENUMERATION:
                checkField(this.enumeration, "enumeration");
                this.enumeration = getEnumerationFromObject();
                this.hasEnumeration = true;
                break;
            case METADATA:
                checkField(this.metadata, "metadata");
                this.metadata = getItemFromObject(object);
                break;
            case CONSTRAINTS:
                checkField(this.constraints, "maxLength");
                this.constraints = getConstraintsTypeFromObject();
                break;
        }
    }

    public Boolean hasEnumeration() {
        return hasEnumeration;
    }

    public Set<FacetTypes> getDefinedFacets() {
        return definedFacets;
    }

    public List<Item> getEnumeration() {
        return enumeration;
    }

    static void checkField(Object key, String fieldName) {
        if (key != null)
            throw new InvalidSchemaException("Field " + fieldName + " is already defined");
    }

    public static String getStringFromObject() throws IOException {
        if (!object.whatIsNext().equals(ValueType.STRING))
            throw new UnexpectedTypeException("Invalid string " + object.read().toString());
        String result = object.readString();
        if (result == null)
            throw new InvalidSchemaException("Invalid null value.");
        return result;
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
            constraints.add(getStringFromObject());
        }
        return constraints;
    }
}
