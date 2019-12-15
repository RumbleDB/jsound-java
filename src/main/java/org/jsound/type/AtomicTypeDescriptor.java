package org.jsound.type;

import jsound.exceptions.InvalidSchemaException;
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
import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.item.Item;
import org.tyson.TYSONValue;
import org.tyson.TysonItem;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.cli.JSoundExecutor.object;
import static org.jsound.facets.FacetTypes.EXPLICIT_TIMEZONE;
import static org.jsound.facets.FacetTypes.FRACTION_DIGITS;
import static org.jsound.facets.FacetTypes.LENGTH;
import static org.jsound.facets.FacetTypes.MAX_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MAX_INCLUSIVE;
import static org.jsound.facets.FacetTypes.MAX_LENGTH;
import static org.jsound.facets.FacetTypes.MIN_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_INCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_LENGTH;
import static org.jsound.facets.FacetTypes.TOTAL_DIGITS;
import static org.jsound.json.SchemaFileJsonParser.commonFacets;


public class AtomicTypeDescriptor extends TypeDescriptor {

    private static final Set<FacetTypes> _allowedFacets = new HashSet<>(
            Arrays.asList(
                LENGTH,
                MIN_LENGTH,
                MAX_LENGTH,
                MIN_INCLUSIVE,
                MAX_INCLUSIVE,
                MIN_EXCLUSIVE,
                MAX_EXCLUSIVE,
                TOTAL_DIGITS,
                FRACTION_DIGITS,
                EXPLICIT_TIMEZONE
            )
    );

    private final AtomicFacets facets;

    protected AtomicTypeDescriptor(ItemTypes type, String name, AtomicFacets facets) {
        super(type, name);
        this.baseType = new TypeOrReference(this);
        this.facets = facets;
    }

    public AtomicTypeDescriptor(ItemTypes type, String name, TypeOrReference baseType, AtomicFacets facets) {
        super(type, name, baseType);
        this.facets = facets;
    }

    @Override
    public boolean isAtomicType() {
        return true;
    }

    @Override
    public AtomicFacets getFacets() {
        return facets;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return this.getBaseType().getAllowedFacets();
    }

    @Override
    public boolean validate(Item item) {
        return false;
    }

    @Override
    public TysonItem annotate(Item item) {
        return new TYSONValue(this.getName(), item);
    }

    public static AtomicTypeDescriptor buildAtomicType(
            AtomicTypes atomicType,
            String name,
            boolean shouldCreateFacets
    )
            throws IOException {
        AtomicFacets facets = null;
        switch (atomicType) {
            case STRING:
                if (shouldCreateFacets)
                    facets = createFacets(StringType._allowedFacets);
                return new StringType(name, facets);
            case INTEGER:
                if (shouldCreateFacets)
                    facets = createFacets(IntegerType._allowedFacets);
                return new IntegerType(name, facets);
            case DECIMAL:
                if (shouldCreateFacets)
                    facets = createFacets(DecimalType._allowedFacets);
                return new DecimalType(name, facets);
            case DOUBLE:
                if (shouldCreateFacets)
                    facets = createFacets(DoubleType._allowedFacets);
                return new DoubleType(name, facets);
            case DURATION:
                if (shouldCreateFacets)
                    facets = createFacets(DurationType._allowedFacets);
                return new DurationType(name, facets);
            case YEARMONTHDURATION:
                if (shouldCreateFacets)
                    facets = createFacets(YearMonthDurationType._allowedFacets);
                return new YearMonthDurationType(name, facets);
            case DAYTIMEDURATION:
                if (shouldCreateFacets)
                    facets = createFacets(DayTimeDurationType._allowedFacets);
                return new DayTimeDurationType(name, facets);
            case DATETIME:
                if (shouldCreateFacets)
                    facets = createFacets(DateTimeType._allowedFacets);
                return new DateTimeType(name, facets);
            case DATE:
                if (shouldCreateFacets)
                    facets = createFacets(DateType._allowedFacets);
                return new DateType(name, facets);
            case TIME:
                if (shouldCreateFacets)
                    facets = createFacets(TimeType._allowedFacets);
                return new TimeType(name, facets);
            case HEXBINARY:
                if (shouldCreateFacets)
                    facets = createFacets(HexBinaryType._allowedFacets);
                return new HexBinaryType(name, facets);
            case BASE64BINARY:
                if (shouldCreateFacets)
                    facets = createFacets(Base64BinaryType._allowedFacets);
                return new Base64BinaryType(name, facets);
            case BOOLEAN:
                if (shouldCreateFacets)
                    facets = createFacets(Collections.emptySet());
                return new BooleanType(name, facets);
            case NULL:
                if (shouldCreateFacets)
                    facets = createFacets(Collections.emptySet());
                return new NullType(name, facets);
            case ANYURI:
                if (shouldCreateFacets)
                    facets = createFacets(AnyURIType._allowedFacets);
                return new AnyURIType(name, facets);
        }
        throw new InvalidSchemaException("Invalid atomic baseType");
    }

    public static AtomicFacets createFacets(Set<FacetTypes> allowedFacets) throws IOException {
        String key;
        AtomicFacets atomicFacets = new AtomicFacets();
        while ((key = object.readObject()) != null) {
            try {
                FacetTypes facetTypes = FacetTypes.valueOf(key);
                if (!(allowedFacets.contains(facetTypes) || commonFacets.contains(facetTypes)))
                    throw new InvalidSchemaException("Invalid facet " + key + ".");
                atomicFacets.setFacet(facetTypes);
            } catch (IllegalArgumentException e) {
                throw new InvalidSchemaException("Invalid facet " + key + ".");
            }
        }
        return atomicFacets;
    }

    public static AtomicFacets createFacets() throws IOException {
        return createFacets(_allowedFacets);
    }

    protected boolean validateLengthFacets(Item item) {
        for (FacetTypes facetType : this.getFacets().getDefinedFacets()) {
            switch (facetType) {
                case LENGTH:
                    if (item.getStringValue().length() != this.getFacets().length)
                        return false;
                    break;
                case MIN_LENGTH:
                    if (item.getStringValue().length() < this.getFacets().minLength)
                        return false;
                    break;
                case MAX_LENGTH:
                    if (item.getStringValue().length() > this.getFacets().maxLength)
                        return false;
                    break;
                case ENUMERATION:
                    try {
                        if (!validateEnumeration(item))
                            return false;
                    } catch (Exception e) {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    protected boolean validateBoundariesFacets(Item item) {
        for (FacetTypes facetType : this.getFacets().getDefinedFacets()) {
            switch (facetType) {
                case MIN_INCLUSIVE:
                    if (!validateMinInclusive(item))
                        return false;
                    break;
                case MIN_EXCLUSIVE:
                    if (!validateMinExclusive(item))
                        return false;
                    break;
                case MAX_INCLUSIVE:
                    if (!validateMaxInclusive(item))
                        return false;
                    break;
                case MAX_EXCLUSIVE:
                    if (!validateMaxExclusive(item))
                        return false;
                    break;
                case ENUMERATION:
                    try {
                        if (!validateEnumeration(item))
                            return false;
                    } catch (Exception e) {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    protected boolean validateDigitsFacets(Item item) {
        for (FacetTypes facetType : this.getFacets().getDefinedFacets()) {
            switch (facetType) {
                case TOTAL_DIGITS:
                    if (item.castToDecimalValue().precision() > this.getFacets().totalDigits)
                        return false;
                    break;
                case FRACTION_DIGITS:
                    if (item.castToDecimalValue().scale() > this.getFacets().fractionDigits)
                        return false;
                    break;
            }
        }
        return true;
    }

    protected boolean validateMinInclusive(Item item) {
        return false;
    }

    protected boolean validateMinExclusive(Item item) {
        return false;
    }

    protected boolean validateMaxInclusive(Item item) {
        return false;
    }

    protected boolean validateMaxExclusive(Item item) {
        return false;
    }

    protected boolean validateEnumeration(Item item) throws Exception {
        String string = item.getStringValue();
        for (Item enumItem : this.getFacets().getEnumeration()) {
            if (string.equals(enumItem.getStringValue()))
                return true;
        }
        return false;
    }
}
