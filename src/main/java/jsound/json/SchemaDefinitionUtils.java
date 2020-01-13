package jsound.json;

import jsound.atomicTypes.AnyURIType;
import jsound.atomicTypes.Base64BinaryType;
import jsound.atomicTypes.BooleanType;
import jsound.atomicTypes.DateTimeType;
import jsound.atomicTypes.DateType;
import jsound.atomicTypes.DayTimeDurationType;
import jsound.atomicTypes.DecimalType;
import jsound.atomicTypes.DoubleType;
import jsound.atomicTypes.DurationType;
import jsound.atomicTypes.HexBinaryType;
import jsound.atomicTypes.IntegerType;
import jsound.atomicTypes.NullType;
import jsound.atomicTypes.StringType;
import jsound.atomicTypes.TimeType;
import jsound.atomicTypes.YearMonthDurationType;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.JsoundException;
import jsound.exceptions.TypeNotResolvedException;
import jsound.exceptions.UnexpectedTypeException;
import jsound.facets.ArrayFacets;
import jsound.facets.FacetTypes;
import jsound.facets.ObjectFacets;
import jsound.facets.UnionFacets;
import jsound.typedescriptors.TypeOrReference;
import jsound.typedescriptors.array.ArrayTypeDescriptor;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.typedescriptors.object.ObjectTypeDescriptor;
import jsound.typedescriptors.union.UnionTypeDescriptor;
import jsound.types.AtomicTypes;
import org.api.TypeDescriptor;

import java.io.IOException;

import static jsound.json.CompactSchemaFileJsonParser.compactSchema;
import static jsound.types.ItemTypes.ATOMIC;
import static org.api.executors.JSoundExecutor.schema;

public class SchemaDefinitionUtils {

    static TypeDescriptor resolveTypeDescriptors(String key) {
        TypeOrReference typeOrReference = compactSchema.get(key);
        if (typeOrReference.getType() == null)
            schema.put(key, createTypeDescriptor(key, resolveTypeDescriptors(typeOrReference.getStringType())));
        else
            schema.put(key, typeOrReference.getType());
        return schema.get(key);
    }

    private static TypeDescriptor createTypeDescriptor(String typeName, TypeDescriptor typeDescriptor) {
        if (typeDescriptor.isAtomicType()) {
            try {
                return AtomicTypeDescriptor.buildAtomicType(
                    AtomicTypes.valueOf(typeDescriptor.getType().name().toUpperCase()),
                    typeName,
                    false
                );
            } catch (IOException e) {
                throw new JsoundException("Error parsing the JSON file");
            }
        } else if (typeDescriptor.isObjectType())
            return new ObjectTypeDescriptor(
                    typeName,
                    new TypeOrReference(typeDescriptor),
                    (ObjectFacets) typeDescriptor.getFacets()
            );
        else if (typeDescriptor.isArrayType())
            return new ArrayTypeDescriptor(
                    typeName,
                    new TypeOrReference(typeDescriptor),
                    (ArrayFacets) typeDescriptor.getFacets()
            );
        else if (typeDescriptor.isUnionType())
            return new UnionTypeDescriptor(typeName, (UnionFacets) typeDescriptor.getFacets());
        throw new UnexpectedTypeException(
                "Cannot infer the baseType " + typeDescriptor.getName() + " for type " + typeName
        );
    }

    static TypeDescriptor resolveSpecificAtomicTypeDescriptor(AtomicTypeDescriptor typeDescriptor) {
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
                typeDescriptor.baseType = new TypeOrReference(resolveSpecificAtomicTypeDescriptor(baseTypeDescriptor));
            else
                typeDescriptor.baseType = new TypeOrReference(baseTypeDescriptor);

            if (typeDescriptor.getType().equals(ATOMIC)) {
                typeDescriptor.setType(typeDescriptor.baseType.getType().getType());
            }

            for (FacetTypes facetType : typeDescriptor.getFacets().getDefinedFacets()) {
                if (
                    !baseTypeDescriptor.getAllowedFacets().contains(facetType)
                        && !SchemaFileJsonParser.commonFacets.contains(facetType)
                )
                    throw new InvalidSchemaException(
                            "Facet " + facetType.name() + " is not valid for type " + typeDescriptor.getName() + "."
                    );
            }

            typeDescriptor = createSpecificAtomicType(typeDescriptor);
        } else if (typeDescriptor.getType().equals(ATOMIC)) {
            typeDescriptor.setType(typeDescriptor.baseType.getType().getType());
            typeDescriptor = createSpecificAtomicType(typeDescriptor);
        }
        schema.put(typeDescriptor.getName(), typeDescriptor);
        return typeDescriptor;
    }

    private static AtomicTypeDescriptor createSpecificAtomicType(AtomicTypeDescriptor atomicTypeDescriptor) {
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
        throw new InvalidSchemaException("Unrecognized baseType.");
    }
}
