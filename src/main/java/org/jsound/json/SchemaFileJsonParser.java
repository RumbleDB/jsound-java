package org.jsound.json;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.JsoundException;
import jsound.exceptions.UnexpectedTypeException;
import org.jsound.api.AtomicTypes;
import org.jsound.api.TypeDescriptor;
import org.jsound.facets.FacetTypes;
import org.jsound.facets.Facets;
import org.jsound.type.Kinds;

import java.io.IOException;
import java.util.*;

import static org.jsound.facets.FacetTypes.*;

public class SchemaFileJsonParser {
    private static Map<String, TypeDescriptor> schema = new HashMap<>();
    private static final Set<FacetTypes> commonFacets = new HashSet<>(
            Arrays.asList(ENUMERATION, METADATA, CONSTRAINTS)
    );

    public static Map<String, TypeDescriptor> getSchema(JsonIterator object) {
        try {
            if (!object.whatIsNext().equals(ValueType.OBJECT)) {
                throw new InvalidSchemaException("The schema root object must be a JSON object");
            }
            String types = object.readObject();
            if (!("types".equals(types))) {
                throw new InvalidSchemaException("There should be a root array of types called \"types\"");
            }
            if (!object.whatIsNext().equals(ValueType.ARRAY)) {
                throw new InvalidSchemaException("Please provide an array of types");
            }
            while (object.readArray()) {
                getTypeDescriptor(object);
            }
            return schema;
        } catch (IOException e) {
            throw new JsoundException("Error parsing the JSON file");
        }
    }

    private static void getTypeDescriptor(JsonIterator object) throws IOException {
        if (object.whatIsNext() != ValueType.OBJECT)
            throw new UnexpectedTypeException(object.read().toString());
        TypeDescriptor typeDescriptor = null;
        String key;
        String name = null;
        Kinds kind = null;
        while ((key = object.readObject()) != null) {
            switch (key) {
                case "name":
                    checkField(name, key);
                    name = getStringFromObject(object);
                    break;
                case "kind":
                    checkName(name);
                    checkField(kind, key);
                    kind = getKindFromObject(object); // consider switch instead of overriding buildTypeDescriptor
                    typeDescriptor = kind.buildTypeDescriptor(name, object);
                    break;
                default:
                    throw new InvalidSchemaException("Please define name and type before " + key);
            }
        }
        if (name == null || kind == null)
            throw new InvalidSchemaException("Missing name or kind");
        schema.put(name, typeDescriptor);
    }

    public static TypeDescriptor buildAtomicTypeDescriptor(String name, JsonIterator object) throws IOException {
        String key, baseType;
        if ((key = object.readObject()) != null) {
            if (!key.equals("baseType"))
                throw new InvalidSchemaException(
                        "Please define the baseType before defining the facets for object " + name
                );
            baseType = getStringFromObject(object);
        } else
            throw new InvalidSchemaException("Invalid schema");
        AtomicTypes atomicType;
        try {
            atomicType = AtomicTypes.valueOf(baseType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidSchemaException("baseType " + baseType + "not defined for atomic kind.");
        }
        return atomicType.buildAtomicType(name, object); // consider switch instead of overriding buildAtomicType
    }

    public static TypeDescriptor buildObjectTypeDescriptor(String name, JsonIterator object) throws IOException {
        return null;
    }

    public static TypeDescriptor buildArrayTypeDescriptor(String name, JsonIterator object) throws IOException {
        return null;
    }

    public static TypeDescriptor buildUnionTypeDescriptor(String name, JsonIterator object) throws IOException {
        return null;
    }


    public static Facets createFacets(Set<FacetTypes> allowedFacets, JsonIterator object) throws IOException {
        String key;
        Facets facets = new Facets();
        while ((key = object.readObject()) != null) {
            try {
                FacetTypes facetTypes = FacetTypes.valueOf(key);
                if (!(allowedFacets.contains(facetTypes) || commonFacets.contains(facetTypes)))
                    throw new InvalidSchemaException("Invalid facet " + key + ".");
                facetTypes.setFacet(facets, object);
            } catch (IllegalArgumentException e) {
                throw new InvalidSchemaException("Invalid facet " + key + ".");
            }
        }
        return facets;
    }

    public static void checkField(Object key, String fieldName) {
        if (key != null)
            throw new InvalidSchemaException("Field " + fieldName + " is already defined");
    }

    private static void checkName(String name) {
        if (name == null)
            throw new InvalidSchemaException("Please define the name before defining the kind");
    }

    public static String getStringFromObject(JsonIterator object) throws IOException {
        if (!object.whatIsNext().equals(ValueType.STRING))
            throw new UnexpectedTypeException("Invalid string " + object.read().toString());
        return object.readString();
    }

    public static Integer getIntegerFromObject(JsonIterator object) throws IOException {
        if (!object.whatIsNext().equals(ValueType.NUMBER))
            throw new UnexpectedTypeException("Invalid number " + object.read().toString());
        return object.readInt();
    }


    private static Kinds getKindFromObject(JsonIterator object) throws IOException {
        if (!object.whatIsNext().equals(ValueType.STRING))
            throw new UnexpectedTypeException("Invalid kind " + object.read().toString());
        String kind = object.readString();
        try {
            return Kinds.valueOf(kind.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnexpectedTypeException("Invalid kind " + object.read().toString());
        }
    }

    public static List<String> getConstraintsTypeFromObject(JsonIterator object) throws IOException {
        if (!object.whatIsNext().equals(ValueType.ARRAY))
            throw new UnexpectedTypeException("Constraints should be an array.");
        List<String> constraints = new ArrayList<>();
        while (object.readArray()) {
            constraints.add(getStringFromObject(object));
        }
        return constraints;
    }
}
