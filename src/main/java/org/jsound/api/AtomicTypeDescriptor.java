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
            String name
    )
            throws IOException {
        Facets facets;
        switch (atomicType) {
            case STRING:
                facets = createFacets(StringType._allowedFacets);
                return new StringType(name, facets);
            case INTEGER:
                facets = createFacets(IntegerType._allowedFacets);
                return new IntegerType(name, facets);
            case DECIMAL:
                facets = createFacets(DecimalType._allowedFacets);
                return new DecimalType(name, facets);
            case DOUBLE:
                facets = createFacets(DoubleType._allowedFacets);
                return new DoubleType(name, facets);
            case DURATION:
                facets = createFacets(DurationType._allowedFacets);
                return new DurationType(name, facets);
            case YEAR_MONTH_DURATION:
                facets = createFacets(YearMonthDurationType._allowedFacets);
                return new YearMonthDurationType(name, facets);
            case DAY_TIME_DURATION:
                facets = createFacets(DayTimeDurationType._allowedFacets);
                return new DayTimeDurationType(name, facets);
            case DATETIME:
                facets = createFacets(DateTimeType._allowedFacets);
                return new DateTimeType(name, facets);
            case DATE:
                facets = createFacets(DateType._allowedFacets);
                return new DateType(name, facets);
            case TIME:
                facets = createFacets(TimeType._allowedFacets);
                return new TimeType(name, facets);
            case HEXBINARY:
                facets = createFacets(HexBinaryType._allowedFacets);
                return new HexBinaryType(name, facets);
            case BASE64BINARY:
                facets = createFacets(Base64BinaryType._allowedFacets);
                return new Base64BinaryType(name, facets);
            case BOOLEAN:
                facets = createFacets(Collections.emptySet());
                return new BooleanType(name, facets);
            case NULL:
                facets = createFacets(Collections.emptySet());
                return new NullType(name, facets);
            case ANY_URI:
                facets = createFacets(AnyURIType._allowedFacets);
                return new AnyURIType(name, facets);
        }
        throw new InvalidSchemaException("Invalid atomic baseType");
    }
}
