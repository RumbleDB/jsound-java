package org.jsound.json;

import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.TypeNotResolvedException;
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
import org.jsound.facets.FacetTypes;
import org.jsound.typedescriptors.TypeDescriptor;
import org.jsound.typedescriptors.TypeOrReference;
import org.jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import org.jsound.types.ItemTypes;

import static org.jsound.cli.JSoundExecutor.schema;
import static org.jsound.json.CompactSchemaFileJsonParser.compactSchema;
import static org.jsound.json.SchemaFileJsonParser.commonFacets;

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

            if (typeDescriptor.getType().equals(ItemTypes.ATOMIC)) {
                typeDescriptor.setType(typeDescriptor.baseType.getType().getType());
            }

            for (FacetTypes facetType : typeDescriptor.getFacets().getDefinedFacets()) {
                if (!baseTypeDescriptor.getAllowedFacets().contains(facetType) && !commonFacets.contains(facetType))
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
