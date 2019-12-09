package org.jsound.api;

import jsound.exceptions.InvalidSchemaException;
import org.jsound.facets.FacetTypes;
import org.jsound.facets.Facets;
import org.jsound.type.AnyURIType;
import org.jsound.type.Base64BinaryType;
import org.jsound.type.BooleanType;
import org.jsound.type.DateTimeType;
import org.jsound.type.DateType;
import org.jsound.type.DayTimeDurationType;
import org.jsound.type.DecimalType;
import org.jsound.type.DoubleType;
import org.jsound.type.DurationType;
import org.jsound.type.HexBinaryType;
import org.jsound.type.IntegerType;
import org.jsound.type.Kinds;
import org.jsound.type.NullType;
import org.jsound.type.StringType;
import org.jsound.type.TimeType;
import org.jsound.type.YearMonthDurationType;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import static org.jsound.json.SchemaFileJsonParser.createFacets;


public class AtomicTypeDescriptor extends TypeDescriptor {

    protected AtomicTypeDescriptor(ItemTypes type, String name, Facets facets) {
        super(type, name, facets);
    }

    public AtomicTypeDescriptor(ItemTypes type, String name, AtomicTypeDescriptor baseType, Facets facets) {
        super(type, name, baseType, facets);
    }

    @Override
    public boolean isAtomicType() {
        return true;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return this.baseType.getAllowedFacets();
    }

    public static AtomicTypeDescriptor buildAtomicType(
            AtomicTypes atomicType,
            String name,
            boolean shouldCreateFacets
    )
            throws IOException {
        Facets facets = null;
        switch (atomicType) {
            case STRING:
                if (shouldCreateFacets)
                    facets = createFacets(StringType._allowedFacets, Kinds.ATOMIC);
                return new StringType(name, facets);
            case INTEGER:
                if (shouldCreateFacets)
                    facets = createFacets(IntegerType._allowedFacets, Kinds.ATOMIC);
                return new IntegerType(name, facets);
            case DECIMAL:
                if (shouldCreateFacets)
                    facets = createFacets(DecimalType._allowedFacets, Kinds.ATOMIC);
                return new DecimalType(name, facets);
            case DOUBLE:
                if (shouldCreateFacets)
                    facets = createFacets(DoubleType._allowedFacets, Kinds.ATOMIC);
                return new DoubleType(name, facets);
            case DURATION:
                if (shouldCreateFacets)
                    facets = createFacets(DurationType._allowedFacets, Kinds.ATOMIC);
                return new DurationType(name, facets);
            case YEARMONTHDURATION:
                if (shouldCreateFacets)
                    facets = createFacets(YearMonthDurationType._allowedFacets, Kinds.ATOMIC);
                return new YearMonthDurationType(name, facets);
            case DAYTIMEDURATION:
                if (shouldCreateFacets)
                    facets = createFacets(DayTimeDurationType._allowedFacets, Kinds.ATOMIC);
                return new DayTimeDurationType(name, facets);
            case DATETIME:
                if (shouldCreateFacets)
                    facets = createFacets(DateTimeType._allowedFacets, Kinds.ATOMIC);
                return new DateTimeType(name, facets);
            case DATE:
                if (shouldCreateFacets)
                    facets = createFacets(DateType._allowedFacets, Kinds.ATOMIC);
                return new DateType(name, facets);
            case TIME:
                if (shouldCreateFacets)
                    facets = createFacets(TimeType._allowedFacets, Kinds.ATOMIC);
                return new TimeType(name, facets);
            case HEXBINARY:
                if (shouldCreateFacets)
                    facets = createFacets(HexBinaryType._allowedFacets, Kinds.ATOMIC);
                return new HexBinaryType(name, facets);
            case BASE64BINARY:
                if (shouldCreateFacets)
                    facets = createFacets(Base64BinaryType._allowedFacets, Kinds.ATOMIC);
                return new Base64BinaryType(name, facets);
            case BOOLEAN:
                if (shouldCreateFacets)
                    facets = createFacets(Collections.emptySet(), Kinds.ATOMIC);
                return new BooleanType(name, facets);
            case NULL:
                if (shouldCreateFacets)
                    facets = createFacets(Collections.emptySet(), Kinds.ATOMIC);
                return new NullType(name, facets);
            case ANYURI:
                if (shouldCreateFacets)
                    facets = createFacets(AnyURIType._allowedFacets, Kinds.ATOMIC);
                return new AnyURIType(name, facets);
        }
        throw new InvalidSchemaException("Invalid atomic baseType");
    }
}
