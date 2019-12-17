package org.jsound.json;

import com.jsoniter.ValueType;
import jsound.exceptions.AlreadyExistingTypeException;
import jsound.exceptions.InvalidKindException;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.JsoundException;
import jsound.exceptions.MissingKindException;
import jsound.exceptions.OverrideBuiltinTypeException;
import jsound.exceptions.TypeNotResolvedException;
import jsound.exceptions.UnexpectedTypeException;
import org.jsound.atomicTypes.AnyURIType;
import org.jsound.atomicTypes.Base64BinaryType;
import org.jsound.atomicTypes.BooleanType;
import org.jsound.atomicTypes.DateTimeType;
import org.jsound.atomicTypes.DateType;
import org.jsound.atomicTypes.DayTimeDurationType;
import org.jsound.atomicTypes.DecimalType;
import org.jsound.atomicTypes.DoubleType;
import org.jsound.atomicTypes.DurationType;
import org.jsound.atomicTypes.HexBinaryType;
import org.jsound.atomicTypes.IntegerType;
import org.jsound.atomicTypes.NullType;
import org.jsound.atomicTypes.StringType;
import org.jsound.atomicTypes.TimeType;
import org.jsound.atomicTypes.YearMonthDurationType;
import org.jsound.facets.ArrayFacets;
import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.facets.Facets;
import org.jsound.facets.ObjectFacets;
import org.jsound.facets.UnionFacets;
import org.jsound.kinds.Kinds;
import org.jsound.type.ArrayTypeDescriptor;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.AtomicTypes;
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
        } catch (IOException e) {
            throw new JsoundException("Error parsing the JSON file");
        }
    }

    private static TypeDescriptor checkType(AtomicTypeDescriptor typeDescriptor) {
        if (typeDescriptor.baseType.getType() == null) {
            AtomicTypeDescriptor baseTypeDescriptor;
            try {
                baseTypeDescriptor = (AtomicTypeDescriptor) schema.getOrDefault(
                    typeDescriptor.baseType.getStringType(),
                    null
                );
            } catch (ClassCastException e) {
                throw new InvalidSchemaException(
                        "BaseType for type " + typeDescriptor.getName() + " should be atomic."
                );
            }
            if (baseTypeDescriptor == null)
                throw new TypeNotResolvedException(
                        "BaseType "
                            + typeDescriptor.baseType.getStringType()
                            + " for type "
                            + typeDescriptor.getName()
                            + " could not be resolved."
                );
            if (baseTypeDescriptor.baseType.getType() == null)
                typeDescriptor.baseType = new TypeOrReference(checkType(baseTypeDescriptor));
            else
                typeDescriptor.baseType = new TypeOrReference(baseTypeDescriptor);

            if (typeDescriptor.getType().equals(ItemTypes.ATOMIC)) {
                typeDescriptor.setType(typeDescriptor.baseType.getType().getType());
            }

            for (FacetTypes facetType : typeDescriptor.getFacets().getDefinedFacets()) {
                if (!baseTypeDescriptor.getAllowedFacets().contains(facetType))
                    throw new InvalidSchemaException(
                            "Facet " + facetType.name() + " is not valid for type " + typeDescriptor.getName() + "."
                    );
            }

            typeDescriptor = castAtomicType(typeDescriptor);
        }
        return typeDescriptor;
    }

    private static AtomicTypeDescriptor castAtomicType(AtomicTypeDescriptor atomicTypeDescriptor) {
        switch (atomicTypeDescriptor.getType()) {
            case ANYURI:
                return new AnyURIType(atomicTypeDescriptor);
            case STRING:
                return new StringType(atomicTypeDescriptor);
            case INTEGER:
                return new IntegerType(atomicTypeDescriptor);
            case DECIMAL:
                return new DecimalType(atomicTypeDescriptor);
            case DOUBLE:
                return new DoubleType(atomicTypeDescriptor);
            case BOOLEAN:
                return new BooleanType(atomicTypeDescriptor);
            case DATETIME:
                return new DateTimeType(atomicTypeDescriptor);
            case DATE:
                return new DateType(atomicTypeDescriptor);
            case TIME:
                return new TimeType(atomicTypeDescriptor);
            case DURATION:
                return new DurationType(atomicTypeDescriptor);
            case YEARMONTHDURATION:
                return new YearMonthDurationType(atomicTypeDescriptor);
            case DAYTIMEDURATION:
                return new DayTimeDurationType(atomicTypeDescriptor);
            case HEXBINARY:
                return new HexBinaryType(atomicTypeDescriptor);
            case BASE64BINARY:
                return new Base64BinaryType(atomicTypeDescriptor);
            case NULL:
                return new NullType(atomicTypeDescriptor);
        }
        throw new InvalidSchemaException("Unrecognized basetype.");
    }

    public static TypeDescriptor getTypeDescriptor(boolean isNested) throws IOException {
        if (object.whatIsNext() != ValueType.OBJECT)
            throw new UnexpectedTypeException(object.read().toString());

        String name = null;
        if (!isNested) {
            if (!"name".equals(object.readObject()))
                throw new InvalidSchemaException("Please specify the \"name\" first.");
            name = getStringFromObject();
            if (schema.containsKey(name))
                throwExistingTypeException(name);
        }

        if (!"kind".equals(object.readObject()))
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
        }
        throw new InvalidKindException("Invalid value for field \"kind\" for type " + name + ".");
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
                        createAtomicFacets(AtomicTypeDescriptor._allowedFacets)
                );
                shouldCheckBaseType.add(atomicTypeDescriptor);
                return atomicTypeDescriptor;
            } else if ("atomic".equals(baseTypeString))
                throw new InvalidSchemaException("BaseType cannot be atomic.");
            AtomicTypeDescriptor atomicTypeDescriptor = new AtomicTypeDescriptor(
                    ItemTypes.ATOMIC,
                    name,
                    new TypeOrReference(baseTypeString),
                    createAtomicFacets(AtomicTypeDescriptor._allowedFacets)
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
                            createObjectFacets()
                    );
                } else if ("object".equals(baseTypeString))
                    return new ObjectTypeDescriptor(
                            name,
                            createObjectFacets()
                    );
                return new ObjectTypeDescriptor(
                        name,
                        new TypeOrReference(baseTypeString),
                        createObjectFacets()
                );
            }
            try {
                FacetTypes facetTypes = FacetTypes.valueOf(key.toUpperCase());
                if (!(ObjectTypeDescriptor._allowedFacets.contains(facetTypes) || commonFacets.contains(facetTypes)))
                    throw new InvalidSchemaException("Invalid facet " + key + ".");
                ObjectFacets facets = new ObjectFacets();
                facets.setFacet(facetTypes);
                return new ObjectTypeDescriptor(
                        name,
                        (ObjectFacets) createFacets(ObjectTypeDescriptor._allowedFacets, facets)
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
                            createArrayFacets()
                    );
                } else if ("array".equals(baseTypeString))
                    return new ArrayTypeDescriptor(name, createArrayFacets());
                return new ArrayTypeDescriptor(
                        name,
                        new TypeOrReference(baseTypeString),
                        createArrayFacets()
                );
            }
            try {
                FacetTypes facetTypes = FacetTypes.valueOf(key.toUpperCase());
                if (!(ArrayTypeDescriptor._allowedFacets.contains(facetTypes) || commonFacets.contains(facetTypes)))
                    throw new InvalidSchemaException("Invalid facet " + key + ".");
                ArrayFacets facets = new ArrayFacets();
                facets.setFacet(facetTypes);
                return new ArrayTypeDescriptor(
                        name,
                        (ArrayFacets) createFacets(ArrayTypeDescriptor._allowedFacets, facets)
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
                            createUnionFacets()
                    );
                } else if ("union".equals(baseTypeString))
                    return new UnionTypeDescriptor(name, createUnionFacets());
                return new UnionTypeDescriptor(
                        name,
                        new TypeOrReference(baseTypeString),
                        createUnionFacets()
                );
            }
            try {
                FacetTypes facetTypes = FacetTypes.valueOf(key.toUpperCase());
                if (!(UnionTypeDescriptor._allowedFacets.contains(facetTypes) || commonFacets.contains(facetTypes)))
                    throw new InvalidSchemaException("Invalid facet " + key + ".");
                UnionFacets facets = new UnionFacets();
                facets.setFacet(facetTypes);
                return new UnionTypeDescriptor(
                        name,
                        (UnionFacets) createFacets(UnionTypeDescriptor._allowedFacets, facets)
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

    static void throwExistingTypeException(String name) {
        try {
            AtomicTypes.valueOf(name);
            throw new OverrideBuiltinTypeException("Builtin types are reserved and cannot be overriden or redefined.");
        } catch (IllegalArgumentException e) {
            throw new AlreadyExistingTypeException(
                    "Two types may not have the same name within an assembled schema set."
            );
        }
    }

    public static AtomicFacets createAtomicFacets(Set<FacetTypes> allowedFacets) throws IOException {
        return (AtomicFacets) createFacets(allowedFacets, new AtomicFacets());
    }

    public static ObjectFacets createObjectFacets() throws IOException {
        return (ObjectFacets) createFacets(ObjectTypeDescriptor._allowedFacets, new ObjectFacets());
    }

    public static ArrayFacets createArrayFacets() throws IOException {
        return (ArrayFacets) createFacets(ArrayTypeDescriptor._allowedFacets, new ArrayFacets());
    }

    public static UnionFacets createUnionFacets() throws IOException {
        return (UnionFacets) createFacets(UnionTypeDescriptor._allowedFacets, new UnionFacets());
    }

    public static Facets createFacets(Set<FacetTypes> allowedFacets, Facets facets) throws IOException {
        String key;
        while ((key = object.readObject()) != null) {
            try {
                FacetTypes facetTypes = FacetTypes.valueOf(key.toUpperCase());
                if (!(allowedFacets.contains(facetTypes) || commonFacets.contains(facetTypes)))
                    throw new InvalidSchemaException("Invalid facet " + key + ".");
                facets.setFacet(facetTypes);
            } catch (IllegalArgumentException e) {
                throw new InvalidSchemaException("Invalid facet " + key + ".");
            }
        }
        return facets;
    }
}
