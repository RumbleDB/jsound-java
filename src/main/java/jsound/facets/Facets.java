package jsound.facets;

import com.jsoniter.ValueType;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.UnexpectedTypeException;
import jsound.typedescriptors.array.ArrayContentDescriptor;
import jsound.typedescriptors.object.FieldDescriptor;
import jsound.typedescriptors.union.UnionContentDescriptor;
import org.api.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.api.executors.JSoundExecutor.jsonSchemaIterator;
import static jsound.json.InstanceFileJsonParser.getItemFromObject;

public class Facets {

    public Item metadata = null;
    public List<Item> enumeration = new ArrayList<>();;
    public List<String> constraints = null;

    public Set<jsound.facets.FacetTypes> definedFacets = new HashSet<>();

    public void setFacet(jsound.facets.FacetTypes facetType, String typeName) throws IOException {
        checkField(facetType);
        switch (facetType) {
            case ENUMERATION:
                setEnumerationFromObject();
                break;
            case METADATA:
                this.metadata = getItemFromObject(jsonSchemaIterator);
                break;
            case CONSTRAINTS:
                this.constraints = getConstraintsTypeFromObject();
                break;
        }
        definedFacets.add(facetType);
    }

    public Set<jsound.facets.FacetTypes> getDefinedFacets() {
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
        if (!jsonSchemaIterator.whatIsNext().equals(ValueType.STRING))
            throw new UnexpectedTypeException(
                    key
                        + " should be a string; "
                        + jsonSchemaIterator.whatIsNext().name().toLowerCase()
                        + " was provided instead."
            );
        return jsonSchemaIterator.readString();
    }

    private void setEnumerationFromObject() throws IOException {
        if (!jsonSchemaIterator.whatIsNext().equals(ValueType.ARRAY))
            throw new UnexpectedTypeException("Enumeration should be an array.");
        while (jsonSchemaIterator.readArray()) {
            this.enumeration.add(getItemFromObject(jsonSchemaIterator));
        }
    }

    private static List<String> getConstraintsTypeFromObject() throws IOException {
        if (!jsonSchemaIterator.whatIsNext().equals(ValueType.ARRAY))
            throw new UnexpectedTypeException("Constraints should be an array.");
        List<String> constraints = new ArrayList<>();
        while (jsonSchemaIterator.readArray()) {
            constraints.add(getStringFromObject("Each constraint"));
        }
        return constraints;
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
