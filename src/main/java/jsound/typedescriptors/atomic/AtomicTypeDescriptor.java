package jsound.typedescriptors.atomic;

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
import jsound.exceptions.LessRestrictiveFacetException;
import jsound.exceptions.UnexpectedTypeException;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import jsound.facets.TimezoneFacet;
import jsound.typedescriptors.TypeOrReference;
import jsound.types.AtomicTypes;
import jsound.types.ItemTypes;
import jsound.tyson.TYSONValue;
import jsound.tyson.TysonItem;
import org.api.Item;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static jsound.facets.FacetTypes.EXPLICIT_TIMEZONE;
import static jsound.facets.FacetTypes.FRACTION_DIGITS;
import static jsound.facets.FacetTypes.LENGTH;
import static jsound.facets.FacetTypes.MAX_EXCLUSIVE;
import static jsound.facets.FacetTypes.MAX_INCLUSIVE;
import static jsound.facets.FacetTypes.MAX_LENGTH;
import static jsound.facets.FacetTypes.MIN_EXCLUSIVE;
import static jsound.facets.FacetTypes.MIN_INCLUSIVE;
import static jsound.facets.FacetTypes.MIN_LENGTH;
import static jsound.facets.FacetTypes.TOTAL_DIGITS;
import static jsound.json.SchemaFileJsonParser.createAtomicFacets;


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
    public boolean validate(ItemWrapper itemWrapper, boolean isEnumValue) {
        return false;
    }

    @Override
    public TysonItem annotate(ItemWrapper itemWrapper) {
        return new TYSONValue(this.getName(), itemWrapper.getItem());
    }

    protected int compare(Item item1, Item item2) {
        return 0;
    }

    @Override
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isAtomicType();
    }

    public static AtomicTypeDescriptor buildAtomicType(
            AtomicTypes atomicType,
            String name,
            boolean shouldCreateAtomicFacets
    )
            throws IOException {
        AtomicFacets facets = null;
        switch (atomicType) {
            case STRING:
                if (shouldCreateAtomicFacets)
                    facets = createAtomicFacets(StringType._allowedFacets, name);
                return new StringType(name, facets);
            case INTEGER:
                if (shouldCreateAtomicFacets)
                    facets = createAtomicFacets(IntegerType._allowedFacets, name);
                return new IntegerType(name, facets);
            case DECIMAL:
                if (shouldCreateAtomicFacets)
                    facets = createAtomicFacets(DecimalType._allowedFacets, name);
                return new DecimalType(name, facets);
            case DOUBLE:
                if (shouldCreateAtomicFacets)
                    facets = createAtomicFacets(DoubleType._allowedFacets, name);
                return new DoubleType(name, facets);
            case DURATION:
                if (shouldCreateAtomicFacets)
                    facets = createAtomicFacets(DurationType._allowedFacets, name);
                return new DurationType(name, facets);
            case YEARMONTHDURATION:
                if (shouldCreateAtomicFacets)
                    facets = createAtomicFacets(YearMonthDurationType._allowedFacets, name);
                return new YearMonthDurationType(name, facets);
            case DAYTIMEDURATION:
                if (shouldCreateAtomicFacets)
                    facets = createAtomicFacets(DayTimeDurationType._allowedFacets, name);
                return new DayTimeDurationType(name, facets);
            case DATETIME:
                if (shouldCreateAtomicFacets)
                    facets = createAtomicFacets(DateTimeType._allowedFacets, name);
                return new DateTimeType(name, facets);
            case DATE:
                if (shouldCreateAtomicFacets)
                    facets = createAtomicFacets(DateType._allowedFacets, name);
                return new DateType(name, facets);
            case TIME:
                if (shouldCreateAtomicFacets)
                    facets = createAtomicFacets(TimeType._allowedFacets, name);
                return new TimeType(name, facets);
            case HEXBINARY:
                if (shouldCreateAtomicFacets)
                    facets = createAtomicFacets(HexBinaryType._allowedFacets, name);
                return new HexBinaryType(name, facets);
            case BASE64BINARY:
                if (shouldCreateAtomicFacets)
                    facets = createAtomicFacets(Base64BinaryType._allowedFacets, name);
                return new Base64BinaryType(name, facets);
            case BOOLEAN:
                if (shouldCreateAtomicFacets)
                    facets = createAtomicFacets(Collections.emptySet(), name);
                return new BooleanType(name, facets);
            case NULL:
                if (shouldCreateAtomicFacets)
                    facets = createAtomicFacets(Collections.emptySet(), name);
                return new NullType(name, facets);
            case ANYURI:
                if (shouldCreateAtomicFacets)
                    facets = createAtomicFacets(AnyURIType._allowedFacets, name);
                return new AnyURIType(name, facets);
            default:
                throw new UnexpectedTypeException("Unexpected type: " + atomicType);
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
                    if (item.getDecimalValue().precision() > this.getFacets().totalDigits)
                        return false;
                    break;
                case FRACTION_DIGITS:
                    if (item.getDecimalValue().scale() > this.getFacets().fractionDigits)
                        return false;
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    protected boolean validateMinInclusive(Item item) {
        return this.compare(item, this.getFacets().minInclusive.getItem()) >= 0;
    }

    protected boolean validateMinExclusive(Item item) {
        return this.compare(item, this.getFacets().minExclusive.getItem()) > 0;
    }

    protected boolean validateMaxInclusive(Item item) {
        return this.compare(item, this.getFacets().maxInclusive.getItem()) <= 0;
    }

    protected boolean validateMaxExclusive(Item item) {
        return this.compare(item, this.getFacets().maxExclusive.getItem()) < 0;
    }

    @Override
    public void resolveAllFacets() {
        if (this.hasResolvedAllFacets)
            return;
        AtomicTypeDescriptor atomicTypeDescriptor = (AtomicTypeDescriptor) this.baseType.getTypeDescriptor();
        if (!this.hasCompatibleType(atomicTypeDescriptor))
            throw new LessRestrictiveFacetException(
                    "Type "
                        + this.getName()
                        + " is not compatible with type "
                        + atomicTypeDescriptor.getName()
            );
        atomicTypeDescriptor.resolveAllFacets();
        resolveAtomicFacets(atomicTypeDescriptor);
        this.hasResolvedAllFacets = true;
    }

    protected void resolveAtomicFacets(AtomicTypeDescriptor baseTypeDescriptor) {
        for (FacetTypes facetTypes : baseTypeDescriptor.getFacets().getDefinedFacets()) {
            if (!this.getFacets().getDefinedFacets().contains(facetTypes)) {
                switch (facetTypes) {
                    case LENGTH:
                        this.getFacets().length = baseTypeDescriptor.getFacets().length;
                        break;
                    case MIN_LENGTH:
                        this.getFacets().minLength = baseTypeDescriptor.getFacets().minLength;
                        break;
                    case MAX_LENGTH:
                        this.getFacets().maxLength = baseTypeDescriptor.getFacets().maxLength;
                        break;
                    case MIN_INCLUSIVE:
                        this.getFacets().minInclusive = baseTypeDescriptor.getFacets().minInclusive;
                        break;
                    case MAX_INCLUSIVE:
                        this.getFacets().maxInclusive = baseTypeDescriptor.getFacets().maxInclusive;
                        break;
                    case MIN_EXCLUSIVE:
                        this.getFacets().minExclusive = baseTypeDescriptor.getFacets().minExclusive;
                        break;
                    case MAX_EXCLUSIVE:
                        this.getFacets().maxExclusive = baseTypeDescriptor.getFacets().maxExclusive;
                        break;
                    case TOTAL_DIGITS:
                        this.getFacets().totalDigits = baseTypeDescriptor.getFacets().totalDigits;
                        break;
                    case FRACTION_DIGITS:
                        this.getFacets().fractionDigits = baseTypeDescriptor.getFacets().fractionDigits;
                        break;
                    case EXPLICIT_TIMEZONE:
                        this.getFacets().explicitTimezone = baseTypeDescriptor.getFacets().explicitTimezone;
                        break;
                    case ENUMERATION:
                    case METADATA:
                    case CONSTRAINTS:
                        resolveCommonFacets(baseTypeDescriptor, facetTypes);
                        break;
                }
            }
        }
    }

    protected void checkBoundariesAndDigitsFacets(TypeDescriptor typeDescriptor) {
        if (this.baseTypeIsChecked)
            return;
        AtomicTypeDescriptor atomicTypeDescriptor = (AtomicTypeDescriptor) typeDescriptor;
        areBoundariesMoreRestrictive(atomicTypeDescriptor.getFacets());
        areDigitsFacetsMoreRestrictive(atomicTypeDescriptor.getFacets());
        this.baseTypeIsChecked = true;
        atomicTypeDescriptor.checkBaseType();
    }

    protected void checkBoundariesAndTimezoneFacets(TypeDescriptor typeDescriptor) {
        if (this.baseTypeIsChecked)
            return;
        AtomicTypeDescriptor atomicTypeDescriptor = (AtomicTypeDescriptor) typeDescriptor;
        areBoundariesMoreRestrictive(atomicTypeDescriptor.getFacets());
        if (this.getFacets().getDefinedFacets().contains(EXPLICIT_TIMEZONE))
            isExplicitTimezoneMoreRestrictive(atomicTypeDescriptor.getFacets());
        this.baseTypeIsChecked = true;
        atomicTypeDescriptor.checkBaseType();
    }

    protected void checkBoundariesFacet(TypeDescriptor typeDescriptor) {
        if (this.baseTypeIsChecked)
            return;
        AtomicTypeDescriptor atomicTypeDescriptor = (AtomicTypeDescriptor) typeDescriptor;
        areBoundariesMoreRestrictive(atomicTypeDescriptor.getFacets());
        this.baseTypeIsChecked = true;
        atomicTypeDescriptor.checkBaseType();
    }

    protected void areLengthFacetsMoreRestrictive(TypeDescriptor typeDescriptor) {
        if (this.baseTypeIsChecked)
            return;
        AtomicTypeDescriptor baseTypeDescriptor = (AtomicTypeDescriptor) typeDescriptor;
        for (FacetTypes facetType : this.getFacets().getDefinedFacets()) {
            switch (facetType) {
                case LENGTH:
                    if (
                        baseTypeDescriptor.facets.getDefinedFacets().contains(LENGTH)
                            && !this.getFacets().length.equals(baseTypeDescriptor.facets.length)
                    )
                        throw new LessRestrictiveFacetException(
                                "Facet length for type "
                                    + this.getName()
                                    + " is not more restrictive than that of its baseType."
                        );
                case MIN_LENGTH:
                    if (
                        baseTypeDescriptor.facets.getDefinedFacets().contains(MIN_LENGTH)
                            && this.getFacets().minLength.compareTo(baseTypeDescriptor.facets.minLength) > 0
                    )
                        throw new LessRestrictiveFacetException(
                                "Facet minLength for type "
                                    + this.getName()
                                    + " is not more restrictive than that of its baseType."
                        );
                case MAX_LENGTH:
                    if (
                        baseTypeDescriptor.facets.getDefinedFacets().contains(MAX_LENGTH)
                            && this.getFacets().maxLength.compareTo(baseTypeDescriptor.facets.maxLength) < 0
                    )
                        throw new LessRestrictiveFacetException(
                                "Facet maxLength for type "
                                    + this.getName()
                                    + " is not more restrictive than that of its baseType."
                        );
                case ENUMERATION:
                    if (!isEnumerationMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException(
                                this.getName() + " is not more restrictive than its baseType."
                        );
            }
        }
        this.baseTypeIsChecked = true;
        baseTypeDescriptor.checkBaseType();
    }

    protected void areBoundariesMoreRestrictive(AtomicFacets facets) {
        for (FacetTypes facetType : this.getFacets().getDefinedFacets()) {
            switch (facetType) {
                case MIN_INCLUSIVE:
                    if (!isMinInclusiveMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException(
                                "Facet minInclusive for type "
                                    + this.getName()
                                    + " is not more restrictive than that of its baseType."
                        );
                case MIN_EXCLUSIVE:
                    if (!isMinExclusiveMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException(
                                "Facet minExclusive for type "
                                    + this.getName()
                                    + " is not more restrictive than that of its baseType."
                        );
                case MAX_INCLUSIVE:
                    if (!isMaxInclusiveMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException(
                                "Facet maxInclusive for type "
                                    + this.getName()
                                    + " is not more restrictive than that of its baseType."
                        );
                case MAX_EXCLUSIVE:
                    if (!isMaxExclusiveMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException(
                                "Facet maxExclusive for type "
                                    + this.getName()
                                    + " is not more restrictive than that of its baseType."
                        );
                case ENUMERATION:
                    if (!isEnumerationMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException(
                                this.getName() + " is not more restrictive than its baseType."
                        );
            }
        }
    }

    protected void areDigitsFacetsMoreRestrictive(AtomicFacets facets) {
        for (FacetTypes facetType : this.getFacets().getDefinedFacets()) {
            switch (facetType) {
                case TOTAL_DIGITS:
                    if (!isTotalDigitsMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException(
                                "Facet totalDigits for type "
                                    + this.getName()
                                    + " is not more restrictive than that of its baseType."
                        );
                case FRACTION_DIGITS:
                    if (!isFractionDigitsMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException(
                                "Facet fractionDigits for type "
                                    + this.getName()
                                    + " is not more restrictive than that of its baseType."
                        );
            }
        }
    }

    protected boolean isMinInclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MIN_INCLUSIVE)
            && compare(this.getFacets().minInclusive.getItem(), facets.minInclusive.getItem()) < 0;
    }

    protected boolean isMinExclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MIN_EXCLUSIVE)
            &&
            compare(this.getFacets().minExclusive.getItem(), facets.minExclusive.getItem()) < 0;
    }

    protected boolean isMaxInclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MAX_INCLUSIVE)
            &&
            compare(this.getFacets().maxInclusive.getItem(), facets.maxInclusive.getItem()) > 0;
    }

    protected boolean isMaxExclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MAX_EXCLUSIVE)
            &&
            compare(this.getFacets().maxExclusive.getItem(), facets.maxExclusive.getItem()) > 0;
    }

    protected boolean isTotalDigitsMoreRestrictive(AtomicFacets facets) {
        return !facets.getDefinedFacets().contains(TOTAL_DIGITS)
            || facets.totalDigits.equals(this.getFacets().totalDigits);
    }

    protected boolean isFractionDigitsMoreRestrictive(AtomicFacets facets) {
        return !facets.getDefinedFacets().contains(FRACTION_DIGITS)
            || facets.fractionDigits.equals(this.getFacets().fractionDigits);
    }

    protected void isExplicitTimezoneMoreRestrictive(AtomicFacets facets) {
        if (
            facets.getDefinedFacets().contains(EXPLICIT_TIMEZONE)
                &&
                !((this.getFacets().explicitTimezone.equals(TimezoneFacet.REQUIRED)
                    || this.getFacets().explicitTimezone.equals(TimezoneFacet.PROHIBITED))
                    &&
                    facets.explicitTimezone.equals(TimezoneFacet.OPTIONAL))
        )
            throw new LessRestrictiveFacetException(
                    "Facet explicitTimezone for type "
                        + this.getName()
                        + " is not more restrictive than that of its baseType."
            );
    }
}
