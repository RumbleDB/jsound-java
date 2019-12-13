package org.jsound.json;

import com.jsoniter.ValueType;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.JsoundException;
import jsound.exceptions.UnexpectedTypeException;
import org.jsound.facets.ArrayFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.facets.ObjectFacets;
import org.jsound.facets.UnionFacets;
import org.jsound.item.Item;
import org.jsound.kinds.Kinds;
import org.jsound.type.ArrayTypeDescriptor;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.AtomicTypes;
import org.jsound.type.FieldDescriptor;
import org.jsound.type.ItemTypes;
import org.jsound.type.ObjectTypeDescriptor;
import org.jsound.type.TypeDescriptor;
import org.jsound.type.TypeOrReference;
import org.jsound.type.UnionTypeDescriptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.jsound.cli.JSoundExecutor.object;
import static org.jsound.cli.JSoundExecutor.schema;
import static org.jsound.facets.FacetTypes.CONSTRAINTS;
import static org.jsound.facets.FacetTypes.ENUMERATION;
import static org.jsound.facets.FacetTypes.METADATA;
import static org.jsound.facets.Facets.getStringFromObject;
import static org.jsound.type.AtomicTypeDescriptor.buildAtomicType;

public class SchemaFileJsonParser {
    public static final Set<FacetTypes> commonFacets = new HashSet<>(
            Arrays.asList(ENUMERATION, METADATA, CONSTRAINTS)
    );

    private static List<AtomicTypeDescriptor> shouldCheckBaseType = new ArrayList<>();

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
                TypeDescriptor typeDescriptor = getTypeDescriptor(false);
                schema.put(typeDescriptor.getName(), typeDescriptor);
            }
            for (AtomicTypeDescriptor atomicTypeDescriptor : shouldCheckBaseType) {
                checkType(atomicTypeDescriptor);
            }
            for (TypeDescriptor typeDescriptor : schema.values()) {
                if (typeDescriptor.getFacets().hasEnumeration()) {
                    checkEnumeration(
                        typeDescriptor.getFacets().getEnumeration(),
                        typeDescriptor.baseType.getTypeDescriptor()
                    );
                }
                if (typeDescriptor.isObjectType()) {
                    ObjectFacets objectFacets = (ObjectFacets) typeDescriptor.getFacets();
                    if (objectFacets.hasDefaultValue())
                        checkDefaultValue(objectFacets.content);
                }
            }
        } catch (IOException e) {
            throw new JsoundException("Error parsing the JSON file");
        }
    }

    private static void checkDefaultValue(Map<String, FieldDescriptor> objectContent) {
        for (FieldDescriptor fieldDescriptor : objectContent.values()) {
            if (!fieldDescriptor.getDefaultValue().isValidAgainst(fieldDescriptor.getType().getTypeDescriptor()))
                throw new InvalidSchemaException("Default value not valid");
        }
    }

    private static void checkEnumeration(List<Item> enumeration, TypeDescriptor typeDescriptor) {
        for (Item item : enumeration) {
            if (!item.isValidAgainst(typeDescriptor))
                throw new InvalidSchemaException(
                        "Enumeration value "
                            + item.getStringAnnotation()
                            + " not valid against type "
                            + typeDescriptor.getType().getTypeName()
                );
        }
    }

    private static TypeDescriptor checkType(TypeDescriptor typeDescriptor) {
        if (typeDescriptor.baseType.getType() == null) {
            TypeDescriptor baseTypeDescriptor = schema.getOrDefault(typeDescriptor.baseType.getStringType(), null);
            if (baseTypeDescriptor == null)
                throw new InvalidSchemaException(
                        "The type " + typeDescriptor.baseType.getStringType() + " does not exist."
                );
            if (baseTypeDescriptor.baseType.getType() == null)
                typeDescriptor.baseType = new TypeOrReference(checkType(baseTypeDescriptor));
            else
                typeDescriptor.baseType = new TypeOrReference(baseTypeDescriptor);
            for (FacetTypes facetType : typeDescriptor.getFacets().getDefinedFacets()) {
                if (!baseTypeDescriptor.getAllowedFacets().contains(facetType))
                    throw new InvalidSchemaException(
                            "Facet " + facetType.name() + " is not valid for type " + typeDescriptor.getName() + "."
                    );
            }
        }
        return typeDescriptor;
    }

    public static TypeDescriptor getTypeDescriptor(boolean isNested) throws IOException {
        if (object.whatIsNext() != ValueType.OBJECT)
            throw new UnexpectedTypeException(object.read().toString());

        String name = null;
        if (!isNested) {
            if (!"name".equals(object.readObject()))
                throw new InvalidSchemaException("Please specify the \"name\" first.");
            name = getStringFromObject();
        }

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
        String key, baseTypeString;
        if ((key = object.readObject()) != null) {
            if (!key.equals("baseType"))
                throw new InvalidSchemaException(
                        "Please define the baseType before defining the facets for object " + name
                );
            baseTypeString = getStringFromObject();
        } else
            throw new InvalidSchemaException("Invalid schema");
        AtomicTypes atomicType;
        try {
            atomicType = AtomicTypes.valueOf(baseTypeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            if (schema.containsKey(baseTypeString)) {
                TypeDescriptor typeDescriptor = schema.get(baseTypeString);
                if (!typeDescriptor.isAtomicType())
                    throw new InvalidSchemaException("The baseType must be atomic.");
                AtomicTypeDescriptor atomicTypeDescriptor = new AtomicTypeDescriptor(
                        typeDescriptor.getType(),
                        name,
                        new TypeOrReference(typeDescriptor),
                        AtomicTypeDescriptor.createFacets()
                );
                shouldCheckBaseType.add(atomicTypeDescriptor);
                return atomicTypeDescriptor;
            } else if ("atomic".equals(baseTypeString))
                throw new InvalidSchemaException("BaseType cannot be atomic.");
            AtomicTypeDescriptor atomicTypeDescriptor = new AtomicTypeDescriptor(
                    ItemTypes.ATOMIC,
                    name,
                    new TypeOrReference(baseTypeString),
                    AtomicTypeDescriptor.createFacets()
            );
            shouldCheckBaseType.add(atomicTypeDescriptor);
            return atomicTypeDescriptor;
        }
        return buildAtomicType(atomicType, name, true);
    }


    private static ObjectTypeDescriptor buildObjectTypeDescriptor(String name) throws IOException {
        String baseTypeString;
        String key = object.readObject();
        if (key != null) {
            if ("baseType".equals(key)) {
                baseTypeString = getStringFromObject();
                if (schema.containsKey(baseTypeString)) {
                    TypeDescriptor typeDescriptor = schema.get(baseTypeString);
                    if (!typeDescriptor.isObjectType())
                        throw new InvalidSchemaException("The baseType must be of type object.");
                    return new ObjectTypeDescriptor(
                            name,
                            new TypeOrReference(typeDescriptor),
                            ObjectTypeDescriptor.createFacets()
                    );
                } else if ("object".equals(baseTypeString))
                    return new ObjectTypeDescriptor(
                            name,
                            ObjectTypeDescriptor.createFacets()
                    );
                return new ObjectTypeDescriptor(
                        name,
                        new TypeOrReference(baseTypeString),
                        ObjectTypeDescriptor.createFacets()
                );
            }
            try {
                FacetTypes facetTypes = FacetTypes.valueOf(key);
                if (!(ObjectTypeDescriptor._allowedFacets.contains(facetTypes) || commonFacets.contains(facetTypes)))
                    throw new InvalidSchemaException("Invalid facet " + key + ".");
                ObjectFacets facets = new ObjectFacets();
                facets.setFacet(facetTypes);
                return new ObjectTypeDescriptor(
                        name,
                        ObjectTypeDescriptor.createFacets(facets)
                );
            } catch (IllegalArgumentException e) {
                throw new InvalidSchemaException("Invalid facet " + key + ".");
            }
        }
        return new ObjectTypeDescriptor(name, new ObjectFacets());
    }

    private static TypeDescriptor buildArrayTypeDescriptor(String name) throws IOException {
        String baseTypeString;
        String key = object.readObject();
        if (key != null) {
            if ("baseType".equals(key)) {
                baseTypeString = getStringFromObject();
                if (schema.containsKey(baseTypeString)) {
                    TypeDescriptor typeDescriptor = schema.get(baseTypeString);
                    if (!typeDescriptor.isArrayType())
                        throw new InvalidSchemaException("The baseType must be of type array.");
                    return new ArrayTypeDescriptor(
                            name,
                            new TypeOrReference(typeDescriptor),
                            ArrayTypeDescriptor.createFacets()
                    );
                } else if ("array".equals(baseTypeString))
                    return new ArrayTypeDescriptor(name, ArrayTypeDescriptor.createFacets());
                return new ArrayTypeDescriptor(
                        name,
                        new TypeOrReference(baseTypeString),
                        ArrayTypeDescriptor.createFacets()
                );
            }
            try {
                FacetTypes facetTypes = FacetTypes.valueOf(key);
                if (!(ArrayTypeDescriptor._allowedFacets.contains(facetTypes) || commonFacets.contains(facetTypes)))
                    throw new InvalidSchemaException("Invalid facet " + key + ".");
                ArrayFacets facets = new ArrayFacets();
                facets.setFacet(facetTypes);
                return new ArrayTypeDescriptor(
                        name,
                        ArrayTypeDescriptor.createFacets(facets)
                );
            } catch (IllegalArgumentException e) {
                throw new InvalidSchemaException("Invalid facet " + key + ".");
            }
        }
        return new ArrayTypeDescriptor(name, new ArrayFacets());
    }

    private static TypeDescriptor buildUnionTypeDescriptor(String name) throws IOException {
        String baseTypeString;
        String key = object.readObject();
        if (key != null) {
            if ("baseType".equals(key)) {
                baseTypeString = getStringFromObject();
                if (schema.containsKey(baseTypeString)) {
                    TypeDescriptor typeDescriptor = schema.get(baseTypeString);
                    if (!typeDescriptor.isUnionType())
                        throw new InvalidSchemaException("The baseType must be of type union.");
                    return new UnionTypeDescriptor(
                            name,
                            new TypeOrReference(typeDescriptor),
                            UnionTypeDescriptor.createFacets()
                    );
                } else if ("union".equals(baseTypeString))
                    return new UnionTypeDescriptor(name, UnionTypeDescriptor.createFacets());
                return new UnionTypeDescriptor(
                        name,
                        new TypeOrReference(baseTypeString),
                        UnionTypeDescriptor.createFacets()
                );
            }
            try {
                FacetTypes facetTypes = FacetTypes.valueOf(key);
                if (!(UnionTypeDescriptor._allowedFacets.contains(facetTypes) || commonFacets.contains(facetTypes)))
                    throw new InvalidSchemaException("Invalid facet " + key + ".");
                UnionFacets facets = new UnionFacets();
                facets.setFacet(facetTypes);
                return new UnionTypeDescriptor(
                        name,
                        UnionTypeDescriptor.createFacets(facets)
                );
            } catch (IllegalArgumentException e) {
                throw new InvalidSchemaException("Invalid facet " + key + ".");
            }
        }
        return new UnionTypeDescriptor(name, new UnionFacets());
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
