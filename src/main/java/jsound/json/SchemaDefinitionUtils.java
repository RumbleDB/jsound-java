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
import jsound.exceptions.TypeNotResolvedException;
import jsound.facets.FacetTypes;
import org.api.TypeDescriptor;
import jsound.typedescriptors.TypeOrReference;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;

import static jsound.json.CompactSchemaFileJsonParser.compactSchema;
import static jsound.types.ItemTypes.ATOMIC;
import static org.api.executors.JSoundExecutor.schema;

public class SchemaDefinitionUtils {

    static TypeDescriptor resolveTypeDescriptors(String key) {
        TypeOrReference typeOrReference = compactSchema.get(key);
        TypeDescriptor typeDescriptor;
        if (typeOrReference.getType() == null)
            typeDescriptor = resolveTypeDescriptors(typeOrReference.getStringType());
        else
            typeDescriptor = typeOrReference.getType();
        schema.put(key, typeDescriptor);
        return typeDescriptor;
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
        }
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
