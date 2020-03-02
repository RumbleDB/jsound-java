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
import jsound.exceptions.CycleInBasetypeException;
import jsound.exceptions.LessRestrictiveFacetException;
import jsound.exceptions.UnexpectedTypeException;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import jsound.facets.TimezoneFacet;
import jsound.typedescriptors.TypeOrReference;
import jsound.types.AtomicTypes;
import jsound.types.ItemTypes;
import jsound.tyson.TYSONItem;
import jsound.tyson.TYSONValue;
import org.api.Item;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static jsound.facets.FacetTypes.EXPLICITTIMEZONE;
import static jsound.facets.FacetTypes.FRACTIONDIGITS;
import static jsound.facets.FacetTypes.LENGTH;
import static jsound.facets.FacetTypes.MAXEXCLUSIVE;
import static jsound.facets.FacetTypes.MAXINCLUSIVE;
import static jsound.facets.FacetTypes.MAXLENGTH;
import static jsound.facets.FacetTypes.MINEXCLUSIVE;
import static jsound.facets.FacetTypes.MININCLUSIVE;
import static jsound.facets.FacetTypes.MINLENGTH;
import static jsound.facets.FacetTypes.TOTALDIGITS;
import static jsound.json.SchemaFileJsonParser.createAtomicFacets;
import static org.api.executors.JSoundExecutor.schema;


public class AtomicTypeDescriptor extends TypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(
            Arrays.asList(
                LENGTH,
                MINLENGTH,
                MAXLENGTH,
                MININCLUSIVE,
                MAXINCLUSIVE,
                MINEXCLUSIVE,
                MAXEXCLUSIVE,
                TOTALDIGITS,
                FRACTIONDIGITS,
                EXPLICITTIMEZONE
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
        return schema.get(this.getName()).validate(itemWrapper, isEnumValue);
    }

    @Override
    public TYSONItem annotate(ItemWrapper itemWrapper) {
        return new TYSONValue(this.getName(), itemWrapper.getItem());
    }

    protected int compare(Item item1, Item item2) {
        return 0;
    }

    @Override
    public boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
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
                case MINLENGTH:
                    if (item.getStringValue().length() < this.getFacets().minLength)
                        return false;
                    break;
                case MAXLENGTH:
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
                case MININCLUSIVE:
                    if (!validateMinInclusive(item))
                        return false;
                    break;
                case MINEXCLUSIVE:
                    if (!validateMinExclusive(item))
                        return false;
                    break;
                case MAXINCLUSIVE:
                    if (!validateMaxInclusive(item))
                        return false;
                    break;
                case MAXEXCLUSIVE:
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
                case TOTALDIGITS:
                    if (item.getDecimalValue().precision() > this.getFacets().totalDigits)
                        return false;
                    break;
                case FRACTIONDIGITS:
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
    public void resolveAllFacets(Set<TypeDescriptor> visitedTypes) {
        if (visitedTypes.contains(this))
            throw new CycleInBasetypeException("There is a cycle in the baseType definitions.");
        visitedTypes.add(this);
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
        atomicTypeDescriptor.resolveAllFacets(visitedTypes);
        resolveAtomicFacets(atomicTypeDescriptor);
        this.hasResolvedAllFacets = true;
    }

    protected void resolveAtomicFacets(AtomicTypeDescriptor baseTypeDescriptor) {
        for (FacetTypes facetTypes : baseTypeDescriptor.getFacets().getDefinedFacets()) {
            if (!this.getFacets().getDefinedFacets().contains(facetTypes)) {
                this.getFacets().definedFacets.add(facetTypes);
                switch (facetTypes) {
                    case LENGTH:
                        this.getFacets().length = baseTypeDescriptor.getFacets().length;
                        break;
                    case MINLENGTH:
                        this.getFacets().minLength = baseTypeDescriptor.getFacets().minLength;
                        break;
                    case MAXLENGTH:
                        this.getFacets().maxLength = baseTypeDescriptor.getFacets().maxLength;
                        break;
                    case MININCLUSIVE:
                        this.getFacets().minInclusive = baseTypeDescriptor.getFacets().minInclusive;
                        break;
                    case MAXINCLUSIVE:
                        this.getFacets().maxInclusive = baseTypeDescriptor.getFacets().maxInclusive;
                        break;
                    case MINEXCLUSIVE:
                        this.getFacets().minExclusive = baseTypeDescriptor.getFacets().minExclusive;
                        break;
                    case MAXEXCLUSIVE:
                        this.getFacets().maxExclusive = baseTypeDescriptor.getFacets().maxExclusive;
                        break;
                    case TOTALDIGITS:
                        this.getFacets().totalDigits = baseTypeDescriptor.getFacets().totalDigits;
                        break;
                    case FRACTIONDIGITS:
                        this.getFacets().fractionDigits = baseTypeDescriptor.getFacets().fractionDigits;
                        break;
                    case EXPLICITTIMEZONE:
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
        if (this.getFacets().getDefinedFacets().contains(EXPLICITTIMEZONE))
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
                case MINLENGTH:
                    if (
                        baseTypeDescriptor.facets.getDefinedFacets().contains(MINLENGTH)
                            && this.getFacets().minLength.compareTo(baseTypeDescriptor.facets.minLength) < 0
                    )
                        throw new LessRestrictiveFacetException(
                                "Facet minLength for type "
                                    + this.getName()
                                    + " is not more restrictive than that of its baseType."
                        );
                case MAXLENGTH:
                    if (
                        baseTypeDescriptor.facets.getDefinedFacets().contains(MAXLENGTH)
                            && this.getFacets().maxLength.compareTo(baseTypeDescriptor.facets.maxLength) > 0
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
                case MININCLUSIVE:
                    if (!isMinInclusiveMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException(
                                "Facet minInclusive for type "
                                    + this.getName()
                                    + " is not more restrictive than that of its baseType."
                        );
                case MINEXCLUSIVE:
                    if (!isMinExclusiveMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException(
                                "Facet minExclusive for type "
                                    + this.getName()
                                    + " is not more restrictive than that of its baseType."
                        );
                case MAXINCLUSIVE:
                    if (!isMaxInclusiveMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException(
                                "Facet maxInclusive for type "
                                    + this.getName()
                                    + " is not more restrictive than that of its baseType."
                        );
                case MAXEXCLUSIVE:
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
                case TOTALDIGITS:
                    if (!isTotalDigitsMoreRestrictive(facets))
                        throw new LessRestrictiveFacetException(
                                "Facet totalDigits for type "
                                    + this.getName()
                                    + " is not more restrictive than that of its baseType."
                        );
                case FRACTIONDIGITS:
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
        return !facets.getDefinedFacets().contains(MININCLUSIVE)
            || compare(this.getFacets().minInclusive.getItem(), facets.minInclusive.getItem()) >= 0;
    }

    protected boolean isMinExclusiveMoreRestrictive(AtomicFacets facets) {
        return !facets.getDefinedFacets().contains(MINEXCLUSIVE)
            || compare(this.getFacets().minExclusive.getItem(), facets.minExclusive.getItem()) >= 0;
    }

    protected boolean isMaxInclusiveMoreRestrictive(AtomicFacets facets) {
        return !facets.getDefinedFacets().contains(MAXINCLUSIVE)
            || compare(this.getFacets().maxInclusive.getItem(), facets.maxInclusive.getItem()) <= 0;
    }

    protected boolean isMaxExclusiveMoreRestrictive(AtomicFacets facets) {
        return !facets.getDefinedFacets().contains(MAXEXCLUSIVE)
            || compare(this.getFacets().maxExclusive.getItem(), facets.maxExclusive.getItem()) <= 0;
    }

    protected boolean isTotalDigitsMoreRestrictive(AtomicFacets facets) {
        return !facets.getDefinedFacets().contains(TOTALDIGITS)
            || this.getFacets().totalDigits.compareTo(facets.totalDigits) <= 0;
    }

    protected boolean isFractionDigitsMoreRestrictive(AtomicFacets facets) {
        return !facets.getDefinedFacets().contains(FRACTIONDIGITS)
            || this.getFacets().fractionDigits.compareTo(facets.fractionDigits) <= 0;
    }

    protected void isExplicitTimezoneMoreRestrictive(AtomicFacets facets) {
        if (
            facets.getDefinedFacets().contains(EXPLICITTIMEZONE)
                &&
                !(this.getFacets().explicitTimezone.equals(facets.explicitTimezone))
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
