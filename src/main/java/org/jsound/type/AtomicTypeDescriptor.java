package org.jsound.type;

import jsound.exceptions.LessRestrictiveFacetException;
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
import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.facets.TimezoneFacet;
import org.jsound.item.Item;
import org.tyson.TYSONValue;
import org.tyson.TysonItem;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
import static org.jsound.json.SchemaFileJsonParser.createAtomicFacets;


public class AtomicTypeDescriptor extends TypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(
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
        this.baseType = null;
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
        return this.getRootBaseType().getAllowedFacets();
    }

    @Override
    public boolean validate(Item item, boolean isEnumerationItem) {
        return false;
    }

    @Override
    public TysonItem annotate(Item item) {
        return new TYSONValue(this.getName(), item);
    }

    public static AtomicTypeDescriptor buildAtomicType(
            AtomicTypes atomicType,
            String name,
            boolean shouldcreateAtomicFacets
    )
            throws IOException {
        AtomicFacets facets = null;
        switch (atomicType) {
            case STRING:
                if (shouldcreateAtomicFacets)
                    facets = createAtomicFacets(StringType._allowedFacets, name);
                return new StringType(name, facets);
            case INTEGER:
                if (shouldcreateAtomicFacets)
                    facets = createAtomicFacets(IntegerType._allowedFacets, name);
                return new IntegerType(name, facets);
            case DECIMAL:
                if (shouldcreateAtomicFacets)
                    facets = createAtomicFacets(DecimalType._allowedFacets, name);
                return new DecimalType(name, facets);
            case DOUBLE:
                if (shouldcreateAtomicFacets)
                    facets = createAtomicFacets(DoubleType._allowedFacets, name);
                return new DoubleType(name, facets);
            case DURATION:
                if (shouldcreateAtomicFacets)
                    facets = createAtomicFacets(DurationType._allowedFacets, name);
                return new DurationType(name, facets);
            case YEARMONTHDURATION:
                if (shouldcreateAtomicFacets)
                    facets = createAtomicFacets(YearMonthDurationType._allowedFacets, name);
                return new YearMonthDurationType(name, facets);
            case DAYTIMEDURATION:
                if (shouldcreateAtomicFacets)
                    facets = createAtomicFacets(DayTimeDurationType._allowedFacets, name);
                return new DayTimeDurationType(name, facets);
            case DATETIME:
                if (shouldcreateAtomicFacets)
                    facets = createAtomicFacets(DateTimeType._allowedFacets, name);
                return new DateTimeType(name, facets);
            case DATE:
                if (shouldcreateAtomicFacets)
                    facets = createAtomicFacets(DateType._allowedFacets, name);
                return new DateType(name, facets);
            case TIME:
                if (shouldcreateAtomicFacets)
                    facets = createAtomicFacets(TimeType._allowedFacets, name);
                return new TimeType(name, facets);
            case HEXBINARY:
                if (shouldcreateAtomicFacets)
                    facets = createAtomicFacets(HexBinaryType._allowedFacets, name);
                return new HexBinaryType(name, facets);
            case BASE64BINARY:
                if (shouldcreateAtomicFacets)
                    facets = createAtomicFacets(Base64BinaryType._allowedFacets, name);
                return new Base64BinaryType(name, facets);
            case BOOLEAN:
                if (shouldcreateAtomicFacets)
                    facets = createAtomicFacets(Collections.emptySet(), name);
                return new BooleanType(name, facets);
            case NULL:
                if (shouldcreateAtomicFacets)
                    facets = createAtomicFacets(Collections.emptySet(), name);
                return new NullType(name, facets);
            case ANYURI:
                if (shouldcreateAtomicFacets)
                    facets = createAtomicFacets(AnyURIType._allowedFacets, name);
                return new AnyURIType(name, facets);
            default:
                throw new UnexpectedTypeException("Unexpected value: " + atomicType);
        }
    }

    protected boolean validateLengthFacets(Item item, boolean isEnumerationItem) {
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
                    if (!validateEnumeration(item, isEnumerationItem))
                        return false;
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    protected boolean validateBoundariesFacets(Item item, boolean isEnumerationItem) {
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
                    if (!validateEnumeration(item, isEnumerationItem))
                        return false;
                    break;
                default:
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
                default:
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

    protected void areBoundariesMoreRestrictive(AtomicFacets facets) {
        for (FacetTypes facetType : this.getFacets().getDefinedFacets()) {
            switch (facetType) {
                case MIN_INCLUSIVE:
                    if (!isMinInclusiveMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException("Facet minInclusive for type " + this.getName() + " is not more restrictive than that of its baseType.");
                case MIN_EXCLUSIVE:
                    if (!isMinExclusiveMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException("Facet minExclusive for type " + this.getName() + " is not more restrictive than that of its baseType.");
                case MAX_INCLUSIVE:
                    if (!isMaxInclusiveMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException("Facet maxInclusive for type " + this.getName() + " is not more restrictive than that of its baseType.");
                case MAX_EXCLUSIVE:
                    if (!isMaxExclusiveMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException("Facet maxExclusive for type " + this.getName() + " is not more restrictive than that of its baseType.");
                case ENUMERATION:
                    if (!isEnumerationMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException(this.getName() + " is not more restrictive than its baseType.");
            }
        }
    }

    protected boolean isMinInclusiveMoreRestrictive(AtomicFacets facets) {
        return false;
    }

    protected boolean isMinExclusiveMoreRestrictive(AtomicFacets facets) {
        return false;
    }

    protected boolean isMaxInclusiveMoreRestrictive(AtomicFacets facets) {
        return false;
    }

    protected boolean isMaxExclusiveMoreRestrictive(AtomicFacets facets) {
        return false;
    }

    protected boolean isEnumerationMoreRestrictive(AtomicFacets facets) {
        return false;
    }

    protected void areDigitsFacetsMoreRestrictive(AtomicFacets facets) {
        for (FacetTypes facetType : this.getFacets().getDefinedFacets()) {
            switch (facetType) {
                case TOTAL_DIGITS:
                    if (!isTotalDigitsMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException("Facet totalDigits for type " + this.getName() + " is not more restrictive than that of its baseType.");
                case FRACTION_DIGITS:
                    if (!isFractionDigitsMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException("Facet fractionDigits for type " + this.getName() + " is not more restrictive than that of its baseType.");
            }
        }
    }

    protected boolean isFractionDigitsMoreRestrictive(AtomicFacets facets) {
        return false;
    }

    protected boolean isTotalDigitsMoreRestrictive(AtomicFacets facets) {
        return false;
    }

    protected void areLengthFacetsMoreRestrictive(AtomicFacets facets) {
        for (FacetTypes facetType : this.getFacets().getDefinedFacets()) {
            switch (facetType) {
                case LENGTH:
                    if (facets.getDefinedFacets().contains(LENGTH) && !this.getFacets().length.equals(facets.length))
                        throw new LessRestrictiveFacetException("Facet length for type " + this.getName() + " is not more restrictive than that of its baseType.");
                case MIN_LENGTH:
                    if (facets.getDefinedFacets().contains(MIN_LENGTH) && this.getFacets().minLength.compareTo(facets.minLength) > 0)
                        throw new LessRestrictiveFacetException("Facet minLength for type " + this.getName() + " is not more restrictive than that of its baseType.");
                case MAX_LENGTH:
                    if (facets.getDefinedFacets().contains(MAX_LENGTH) && this.getFacets().maxLength.compareTo(facets.maxLength) < 0)
                        throw new LessRestrictiveFacetException("Facet maxLength for type " + this.getName() + " is not more restrictive than that of its baseType.");
                case ENUMERATION:
                    if (!isEnumerationMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException(this.getName() + " is not more restrictive than its baseType.");
            }
        }
    }

    protected void isExplicitTimezoneMoreRestrictive(AtomicFacets facets) {
        if (facets.getDefinedFacets().contains(EXPLICIT_TIMEZONE) &&
                !((this.getFacets().explicitTimezone.equals(TimezoneFacet.REQUIRED) || this.getFacets().explicitTimezone.equals(TimezoneFacet.PROHIBITED)) &&
                        facets.explicitTimezone.equals(TimezoneFacet.OPTIONAL)))
            throw new LessRestrictiveFacetException("Facet explicitTimezone for type " + this.getName() + " is not more restrictive than that of its baseType.");
    }
}
