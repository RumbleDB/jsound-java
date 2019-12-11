package org.jsound.json;

import com.jsoniter.ValueType;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.JsoundException;
import jsound.exceptions.UnexpectedTypeException;
import org.jsound.facets.FacetTypes;
import org.jsound.facets.Facets;
import org.jsound.kinds.Kinds;
import org.jsound.type.ArrayTypeDescriptor;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.AtomicTypes;
import org.jsound.type.ObjectTypeDescriptor;
import org.jsound.type.TypeDescriptor;
import org.jsound.type.UnionTypeDescriptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.cli.JSoundExecutor.object;
import static org.jsound.cli.JSoundExecutor.schema;
import static org.jsound.facets.FacetTypes.CONSTRAINTS;
import static org.jsound.facets.FacetTypes.ENUMERATION;
import static org.jsound.facets.FacetTypes.METADATA;
import static org.jsound.facets.Facets.getStringFromObject;
import static org.jsound.type.AtomicTypeDescriptor.buildAtomicType;

public class SchemaFileJsonParser {
    private static final Set<FacetTypes> commonFacets = new HashSet<>(
            Arrays.asList(ENUMERATION, METADATA, CONSTRAINTS)
    );

    public static void createSchema() {
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
                TypeDescriptor typeDescriptor = getTypeDescriptor();
                schema.put(typeDescriptor.getName(), typeDescriptor);
            }
        } catch (IOException e) {
            throw new JsoundException("Error parsing the JSON file");
        }
    }

    public static TypeDescriptor getTypeDescriptor() throws IOException {
        if (object.whatIsNext() != ValueType.OBJECT)
            throw new UnexpectedTypeException(object.read().toString());

        if (!"name".equals(object.readObject()))
            throw new InvalidSchemaException("Please specify the \"name\" first.");
        String name = getStringFromObject();

        if (!"kind".equals(object.readObject()))
            throw new InvalidSchemaException("Please specify the \"kind\" before other properties.");

        return buildTypeDescriptor(name);
    }

    private static TypeDescriptor buildTypeDescriptor(String name) throws IOException {
        Kinds kind = getKindFromObject();
        switch (kind) {
            case ATOMIC:
                return buildAtomicTypeDescriptor(name);
            case OBJECT:
                return buildObjectTypeDescriptor(name);
            case ARRAY:
                return buildArrayTypeDescriptor(name);
            case UNION:
                return buildUnionTypeDescriptor(name);
        }
        throw new InvalidSchemaException("Invalid kind.");
    }

    private static TypeDescriptor buildAtomicTypeDescriptor(String name) throws IOException {
        String key, baseType;
        if ((key = object.readObject()) != null) {
            if (!key.equals("baseType"))
                throw new InvalidSchemaException(
                        "Please define the baseType before defining the facets for object " + name
                );
            baseType = getStringFromObject();
        } else
            throw new InvalidSchemaException("Invalid schema");
        AtomicTypes atomicType;
        try {
            atomicType = AtomicTypes.valueOf(baseType.toUpperCase());
        } catch (IllegalArgumentException e) {
            if (schema.containsKey(baseType)) {
                TypeDescriptor typeDescriptor = schema.get(baseType);
                if (!typeDescriptor.isAtomicType())
                    throw new InvalidSchemaException("The baseType must be atomic.");
                return new AtomicTypeDescriptor(
                        typeDescriptor.getType(),
                        name,
                        (AtomicTypeDescriptor) typeDescriptor,
                        createFacets(typeDescriptor.getAllowedFacets(), Kinds.ATOMIC)
                );
            } else if ("atomic".equals(baseType))
                throw new InvalidSchemaException("BaseType cannot be atomic.");
            throw new InvalidSchemaException("Type " + baseType + " not defined for " + name);
        }
        return buildAtomicType(atomicType, name, true);
    }


    private static ObjectTypeDescriptor buildObjectTypeDescriptor(String name) throws IOException {
        String baseType;
        String key = object.readObject();
        if (key != null) {
            if ("baseType".equals(key)) {
                baseType = getStringFromObject();
                if (schema.containsKey(baseType)) {
                    TypeDescriptor typeDescriptor = schema.get(baseType);
                    if (!typeDescriptor.isObjectType())
                        throw new InvalidSchemaException("The baseType must be of atomicTypes object.");
                    return new ObjectTypeDescriptor(
                            name,
                            (ObjectTypeDescriptor) typeDescriptor,
                            createFacets(ObjectTypeDescriptor._allowedFacets, Kinds.OBJECT)
                    );
                } else if ("object".equals(baseType))
                    return new ObjectTypeDescriptor(
                            name,
                            createFacets(ObjectTypeDescriptor._allowedFacets, Kinds.OBJECT)
                    );
                throw new InvalidSchemaException("Type " + baseType + " not defined for " + name);
            }
            try {
                FacetTypes facetTypes = FacetTypes.valueOf(key);
                if (!(ObjectTypeDescriptor._allowedFacets.contains(facetTypes) || commonFacets.contains(facetTypes)))
                    throw new InvalidSchemaException("Invalid facet " + key + ".");
                Facets facets = new Facets();
                facets.setFacet(facetTypes, object, Kinds.OBJECT);
                return new ObjectTypeDescriptor(
                        name,
                        createFacets(facets, ObjectTypeDescriptor._allowedFacets, Kinds.OBJECT)
                );
            } catch (IllegalArgumentException e) {
                throw new InvalidSchemaException("Invalid facet " + key + ".");
            }
        }
        return new ObjectTypeDescriptor(name, new Facets());
    }

    private static TypeDescriptor buildArrayTypeDescriptor(String name) throws IOException {
        String baseType;
        String key = object.readObject();
        if (key != null) {
            if ("baseType".equals(key)) {
                baseType = getStringFromObject();
                if (schema.containsKey(baseType)) {
                    TypeDescriptor typeDescriptor = schema.get(baseType);
                    if (!typeDescriptor.isArrayType())
                        throw new InvalidSchemaException("The baseType must be of atomicTypes array.");
                    return new ArrayTypeDescriptor(
                            name,
                            (ArrayTypeDescriptor) typeDescriptor,
                            createFacets(ArrayTypeDescriptor._allowedFacets, Kinds.ARRAY)
                    );
                } else if ("array".equals(baseType))
                    return new ArrayTypeDescriptor(name, createFacets(ArrayTypeDescriptor._allowedFacets, Kinds.ARRAY));
                throw new InvalidSchemaException("Type " + baseType + " not defined for " + name);
            }
            try {
                FacetTypes facetTypes = FacetTypes.valueOf(key);
                if (!(ArrayTypeDescriptor._allowedFacets.contains(facetTypes) || commonFacets.contains(facetTypes)))
                    throw new InvalidSchemaException("Invalid facet " + key + ".");
                Facets facets = new Facets();
                facets.setFacet(facetTypes, object, Kinds.ARRAY);
                return new ArrayTypeDescriptor(
                        name,
                        createFacets(facets, ArrayTypeDescriptor._allowedFacets, Kinds.ARRAY)
                );
            } catch (IllegalArgumentException e) {
                throw new InvalidSchemaException("Invalid facet " + key + ".");
            }
        }
        return new ArrayTypeDescriptor(name, new Facets());
    }

    private static TypeDescriptor buildUnionTypeDescriptor(String name) throws IOException {
        String baseType;
        String key = object.readObject();
        if (key != null) {
            if ("baseType".equals(key)) {
                baseType = getStringFromObject();
                if (schema.containsKey(baseType)) {
                    TypeDescriptor typeDescriptor = schema.get(baseType);
                    if (!typeDescriptor.isArrayType())
                        throw new InvalidSchemaException("The baseType must be of atomicTypes array.");
                    return new UnionTypeDescriptor(
                            name,
                            (UnionTypeDescriptor) typeDescriptor,
                            createFacets(UnionTypeDescriptor._allowedFacets, Kinds.UNION)
                    );
                } else if ("union".equals(baseType))
                    return new UnionTypeDescriptor(name, createFacets(UnionTypeDescriptor._allowedFacets, Kinds.UNION));
                throw new InvalidSchemaException("Type " + baseType + " not defined for " + name);
            }
            try {
                FacetTypes facetTypes = FacetTypes.valueOf(key);
                if (!(UnionTypeDescriptor._allowedFacets.contains(facetTypes) || commonFacets.contains(facetTypes)))
                    throw new InvalidSchemaException("Invalid facet " + key + ".");
                Facets facets = new Facets();
                facets.setFacet(facetTypes, object, Kinds.UNION);
                return new UnionTypeDescriptor(
                        name,
                        createFacets(facets, UnionTypeDescriptor._allowedFacets, Kinds.UNION)
                );
            } catch (IllegalArgumentException e) {
                throw new InvalidSchemaException("Invalid facet " + key + ".");
            }
        }
        return new UnionTypeDescriptor(name, new Facets());
    }

    public static Facets createFacets(Facets facets, Set<FacetTypes> allowedFacets, Kinds kind) throws IOException {
        String key;
        while ((key = object.readObject()) != null) {
            try {
                FacetTypes facetTypes = FacetTypes.valueOf(key);
                if (!(allowedFacets.contains(facetTypes) || commonFacets.contains(facetTypes)))
                    throw new InvalidSchemaException("Invalid facet " + key + ".");
                facets.setFacet(facetTypes, object, kind);
            } catch (IllegalArgumentException e) {
                throw new InvalidSchemaException("Invalid facet " + key + ".");
            }
        }
        return facets;
    }

    public static Facets createFacets(Set<FacetTypes> allowedFacets, Kinds kind) throws IOException {
        return createFacets(new Facets(), allowedFacets, kind);
    }

    private static Kinds getKindFromObject() throws IOException {
        String kind = getStringFromObject();
        try {
            return Kinds.valueOf(kind.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnexpectedTypeException("Invalid kind " + kind);
        }
    }
}
