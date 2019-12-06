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
import org.jsound.type.TypeFactory;

import java.io.IOException;
import java.util.*;

import static org.jsound.api.AtomicTypes.*;
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

        if (!"name".equals(object.readObject()))
            throw new InvalidSchemaException("Please specify the \"name\" first.");
        String name = getStringFromObject(object);

        if (!"kind".equals(object.readObject()))
            throw new InvalidSchemaException("Please specify the \"kind\" before other properties.");

        schema.put(name, buildTypeDescriptor(name, object));
    }

    private static TypeDescriptor buildTypeDescriptor(String name, JsonIterator object) throws IOException {
        Kinds kind = getKindFromObject(object);
        switch (kind) {
            case ATOMIC:
                return buildAtomicTypeDescriptor(name, object);
            case OBJECT:
                return buildObjectTypeDescriptor(name, object);
            case ARRAY:
                return buildArrayTypeDescriptor(name, object);
            case UNION:
                return buildUnionTypeDescriptor(name, object);
        }
        throw new InvalidSchemaException("Invalid kind.");
    }

    private static TypeDescriptor buildAtomicTypeDescriptor(String name, JsonIterator object) throws IOException {
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
        return buildAtomicType(atomicType, name, object);
    }

    private static TypeDescriptor buildAtomicType(
            AtomicTypes atomicType,
            String name,
            JsonIterator object
    )
            throws IOException {
        switch (atomicType) {
            case STRING:
                return new TypeDescriptor(
                        TypeFactory.getInstance()
                            .createStringType(name, createFacets(STRING.getAllowedFacets(), object))
                );
            case INTEGER:
                return new TypeDescriptor(
                        TypeFactory.getInstance()
                            .createIntegerType(name, createFacets(INTEGER.getAllowedFacets(), object))
                );
            case DECIMAL:
                return new TypeDescriptor(
                        TypeFactory.getInstance()
                            .createDecimalType(name, createFacets(DECIMAL.getAllowedFacets(), object))
                );
            case DOUBLE:
                return new TypeDescriptor(
                        TypeFactory.getInstance()
                            .createDoubleType(name, createFacets(DOUBLE.getAllowedFacets(), object))
                );
            case HEXBINARY:
                return new TypeDescriptor(
                        TypeFactory.getInstance()
                            .createHexBinaryType(name, createFacets(HEXBINARY.getAllowedFacets(), object))
                );
            case BASE64BINARY:
                return new TypeDescriptor(
                        TypeFactory.getInstance()
                            .createBase64BinaryType(name, createFacets(BASE64BINARY.getAllowedFacets(), object))
                );
            case ANYURI:
                return new TypeDescriptor(
                        TypeFactory.getInstance()
                            .createAnyURIType(name, createFacets(ANYURI.getAllowedFacets(), object))
                );
        }
        throw new InvalidSchemaException("Invalid atomic baseType");
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
        String result = object.readString();
        if (result == null)
            throw new InvalidSchemaException("Invalid null value.");
        return result;
    }

    public static Integer getIntegerFromObject(JsonIterator object) throws IOException {
        if (!object.whatIsNext().equals(ValueType.NUMBER))
            throw new UnexpectedTypeException("Invalid number " + object.read().toString());
        return object.readInt();
    }


    private static Kinds getKindFromObject(JsonIterator object) throws IOException {
        String kind = getStringFromObject(object);
        try {
            return Kinds.valueOf(kind.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnexpectedTypeException("Invalid kind " + kind);
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
