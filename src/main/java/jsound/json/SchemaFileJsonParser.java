package jsound.json;

import com.jsoniter.ValueType;
import jsound.exceptions.AlreadyExistingTypeException;
import jsound.exceptions.InconsistentBaseTypeException;
import jsound.exceptions.InvalidKindException;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.JsoundException;
import jsound.exceptions.MissingKindException;
import jsound.exceptions.OverrideBuiltinTypeException;
import jsound.exceptions.UnexpectedTypeException;
import jsound.facets.ArrayFacets;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import jsound.facets.Facets;
import jsound.facets.ObjectFacets;
import jsound.facets.UnionFacets;
import jsound.kinds.Kinds;
import jsound.typedescriptors.TypeOrReference;
import jsound.typedescriptors.array.ArrayTypeDescriptor;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.typedescriptors.object.ObjectTypeDescriptor;
import jsound.typedescriptors.union.UnionTypeDescriptor;
import jsound.types.AtomicTypes;
import jsound.types.ItemTypes;
import org.api.TypeDescriptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.api.executors.JSoundExecutor.jsonSchemaIterator;
import static org.api.executors.JSoundExecutor.schema;

public class SchemaFileJsonParser {
    public static final Set<FacetTypes> commonFacets = new HashSet<>(
            Arrays.asList(FacetTypes.ENUMERATION, FacetTypes.METADATA, FacetTypes.CONSTRAINTS)
    );

    public static List<AtomicTypeDescriptor> shouldCheckBaseType;

    public static void createSchema() {
        shouldCheckBaseType = new ArrayList<>();
        try {
            if (!jsonSchemaIterator.whatIsNext().equals(ValueType.OBJECT)) {
                throw new InvalidSchemaException("The schema root object must be a JSON object");
            }
            String types = jsonSchemaIterator.readObject();
            if (!"types".equals(types)) {
                throw new InvalidSchemaException("There should be a root array of types called \"types\"");
            }
            if (!jsonSchemaIterator.whatIsNext().equals(ValueType.ARRAY)) {
                throw new InvalidSchemaException("Please provide an array of types");
            }
            while (jsonSchemaIterator.readArray()) {
                TypeDescriptor typeDescriptor = getTypeDescriptor(false);
                schema.put(typeDescriptor.getName(), typeDescriptor);
            }
            for (AtomicTypeDescriptor atomicTypeDescriptor : shouldCheckBaseType) {
                SchemaDefinitionUtils.resolveSpecificAtomicTypeDescriptor(atomicTypeDescriptor);
            }
        } catch (IOException e) {
            throw new JsoundException("Error parsing the JSON file");
        }
    }

    public static TypeDescriptor getTypeDescriptor(boolean isNested) throws IOException {
        if (!jsonSchemaIterator.whatIsNext().equals(ValueType.OBJECT))
            throw new UnexpectedTypeException(jsonSchemaIterator.read().toString());

        String name = null;
        if (!isNested) {
            if (!"name".equals(jsonSchemaIterator.readObject()))
                throw new InvalidSchemaException("Please specify the \"name\" first.");
            name = Facets.getStringFromObject("name");
            if (schema.containsKey(name))
                throwExistingTypeException(name);
        }

        if (!"kind".equals(jsonSchemaIterator.readObject()))
            throw new MissingKindException(
                    "Field \"kind\" is missing for object " + name + " or is defined after other properties."
            );

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
            default:
                throw new InvalidKindException(
                        "Kind should be one of \"atomic\", \"object\", \"array\", \"union\". "
                            + kind
                            + " was given instead."
                );
        }
    }

    private static TypeDescriptor buildAtomicTypeDescriptor(String name) throws IOException {
        String key, baseTypeString;
        if ((key = jsonSchemaIterator.readObject()) != null) {
            if (!key.equals("baseType"))
                throw new InconsistentBaseTypeException(
                        "Please define the baseType before defining the facets for type " + name
                );
            baseTypeString = Facets.getStringFromObject("baseType");
        } else
            throw new InvalidSchemaException("Invalid schema");
        AtomicTypes atomicType;
        try {
            atomicType = AtomicTypes.valueOf(baseTypeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return createNonPrimitiveAtomicTypeDescriptor(name, baseTypeString);
        }
        return AtomicTypeDescriptor.buildAtomicType(atomicType, name, true);
    }

    private static AtomicTypeDescriptor createNonPrimitiveAtomicTypeDescriptor(String name, String baseTypeString)
            throws IOException {
        if (schema.containsKey(baseTypeString)) {
            TypeDescriptor typeDescriptor = schema.get(baseTypeString);
            if (!typeDescriptor.isAtomicType())
                throw new InvalidSchemaException("The baseType must be atomic.");
            AtomicTypeDescriptor atomicTypeDescriptor = new AtomicTypeDescriptor(
                    ItemTypes.ATOMIC,
                    name,
                    new TypeOrReference(typeDescriptor),
                    createAtomicFacets(AtomicTypeDescriptor._allowedFacets, name)
            );
            shouldCheckBaseType.add(atomicTypeDescriptor);
            return atomicTypeDescriptor;
        } else if ("atomic".equals(baseTypeString))
            throw new InvalidSchemaException("BaseType cannot be atomic.");
        AtomicTypeDescriptor atomicTypeDescriptor = new AtomicTypeDescriptor(
                ItemTypes.ATOMIC,
                name,
                new TypeOrReference(baseTypeString),
                createAtomicFacets(AtomicTypeDescriptor._allowedFacets, name)
        );
        shouldCheckBaseType.add(atomicTypeDescriptor);
        return atomicTypeDescriptor;
    }


    private static ObjectTypeDescriptor buildObjectTypeDescriptor(String name) throws IOException {
        String baseTypeString;
        String key = jsonSchemaIterator.readObject();
        if (key == null)
            return new ObjectTypeDescriptor(name, new ObjectFacets());

        if ("baseType".equals(key)) {
            baseTypeString = Facets.getStringFromObject("baseType");
            if (schema.containsKey(baseTypeString)) {
                TypeDescriptor typeDescriptor = schema.get(baseTypeString);
                if (!typeDescriptor.isObjectType())
                    throw new InvalidSchemaException("The baseType must be of type object.");
                return new ObjectTypeDescriptor(
                        name,
                        new TypeOrReference(typeDescriptor),
                        createObjectFacets(name)
                );
            } else if ("object".equals(baseTypeString))
                return new ObjectTypeDescriptor(
                        name,
                        createObjectFacets(name)
                );
            return new ObjectTypeDescriptor(
                    name,
                    new TypeOrReference(baseTypeString),
                    createObjectFacets(name)
            );
        }
        try {
            FacetTypes facetTypes = FacetTypes.valueOf(key.toUpperCase());
            if (!(ObjectTypeDescriptor._allowedFacets.contains(facetTypes) || commonFacets.contains(facetTypes)))
                throw new InvalidSchemaException("Invalid facet " + key + ".");
            ObjectFacets facets = new ObjectFacets();
            facets.setFacet(facetTypes, name);
            return new ObjectTypeDescriptor(
                    name,
                    (ObjectFacets) createFacets(ObjectTypeDescriptor._allowedFacets, facets, name)
            );
        } catch (IllegalArgumentException e) {
            throw new InvalidSchemaException("Invalid facet " + key + ".");
        }

    }

    private static TypeDescriptor buildArrayTypeDescriptor(String name) throws IOException {
        String baseTypeString;
        String key = jsonSchemaIterator.readObject();
        if (key == null)
            return new ArrayTypeDescriptor(name, new ArrayFacets());
        if ("baseType".equals(key)) {
            baseTypeString = Facets.getStringFromObject("baseType");
            if (schema.containsKey(baseTypeString)) {
                TypeDescriptor typeDescriptor = schema.get(baseTypeString);
                if (!typeDescriptor.isArrayType())
                    throw new InvalidSchemaException("The baseType must be of type array.");
                return new ArrayTypeDescriptor(
                        name,
                        new TypeOrReference(typeDescriptor),
                        createArrayFacets(name)
                );
            } else if ("array".equals(baseTypeString))
                return new ArrayTypeDescriptor(name, createArrayFacets(name));
            return new ArrayTypeDescriptor(
                    name,
                    new TypeOrReference(baseTypeString),
                    createArrayFacets(name)
            );
        }
        try {
            FacetTypes facetTypes = FacetTypes.valueOf(key.toUpperCase());
            if (!(ArrayTypeDescriptor._allowedFacets.contains(facetTypes) || commonFacets.contains(facetTypes)))
                throw new InvalidSchemaException("Invalid facet " + key + ".");
            ArrayFacets facets = new ArrayFacets();
            facets.setFacet(facetTypes, name);
            return new ArrayTypeDescriptor(
                    name,
                    (ArrayFacets) createFacets(ArrayTypeDescriptor._allowedFacets, facets, name)
            );
        } catch (IllegalArgumentException e) {
            throw new InvalidSchemaException("Invalid facet " + key + ".");
        }
    }

    private static TypeDescriptor buildUnionTypeDescriptor(String name) throws IOException {
        String key = jsonSchemaIterator.readObject();
        if (key == null)
            return new UnionTypeDescriptor(name, new UnionFacets());
        try {
            FacetTypes facetTypes = FacetTypes.valueOf(key.toUpperCase());
            if (!(UnionTypeDescriptor._allowedFacets.contains(facetTypes) || commonFacets.contains(facetTypes)))
                throw new InvalidSchemaException("Invalid facet " + key + ".");
            UnionFacets facets = new UnionFacets();
            facets.setFacet(facetTypes, name);
            return new UnionTypeDescriptor(
                    name,
                    (UnionFacets) createFacets(UnionTypeDescriptor._allowedFacets, facets, name)
            );
        } catch (IllegalArgumentException e) {
            throw new InvalidSchemaException("Invalid facet " + key + ".");
        }
    }

    private static Kinds getKindFromObject() throws IOException {
        String kind = Facets.getStringFromObject("kind");
        try {
            return Kinds.valueOf(kind.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidKindException(
                    "Kind should be one of \"atomic\", \"object\", \"array\", \"union\". "
                        + kind
                        + " was given instead."
            );
        }
    }

    static void throwExistingTypeException(String name) {
        try {
            AtomicTypes.valueOf(name.toUpperCase());
            throw new OverrideBuiltinTypeException("Builtin types are reserved and cannot be overridden or redefined.");
        } catch (IllegalArgumentException e) {
            throw new AlreadyExistingTypeException(
                    "Two types may not have the same name within an assembled schema set."
            );
        }
    }

    public static AtomicFacets createAtomicFacets(Set<FacetTypes> allowedFacets, String typeName)
            throws IOException {
        return (AtomicFacets) createFacets(allowedFacets, new AtomicFacets(), typeName);
    }

    public static ObjectFacets createObjectFacets(String typeName) throws IOException {
        return (ObjectFacets) createFacets(ObjectTypeDescriptor._allowedFacets, new ObjectFacets(), typeName);
    }

    public static ArrayFacets createArrayFacets(String typeName) throws IOException {
        return (ArrayFacets) createFacets(ArrayTypeDescriptor._allowedFacets, new ArrayFacets(), typeName);
    }

    public static Facets createFacets(Set<FacetTypes> allowedFacets, Facets facets, String typeName)
            throws IOException {
        String key;
        while ((key = jsonSchemaIterator.readObject()) != null) {
            try {
                FacetTypes facetTypes = FacetTypes.valueOf(key.toUpperCase());
                if (!(allowedFacets.contains(facetTypes) || commonFacets.contains(facetTypes)))
                    throw new InvalidSchemaException("Invalid facet " + key + ".");
                facets.setFacet(facetTypes, typeName);
            } catch (IllegalArgumentException e) {
                throw new UnexpectedTypeException("Invalid value for facet " + key + ".");
            }
        }
        return facets;
    }
}
