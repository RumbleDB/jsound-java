package org.jsound.facets;

import com.jsoniter.ValueType;
import jsound.exceptions.UnexpectedTypeException;

import java.io.IOException;

import static org.jsound.cli.JSoundExecutor.object;

public class AtomicFacets extends Facets {
    private Integer length = null, minLength = null, maxLength = null;
    private String minInclusive = null, maxInclusive = null, minExclusive = null, maxExclusive = null;
    private Integer totalDigits = null, fractionDigits = null;
    private TimezoneFacet explicitTimezone = null;

    @Override
    public void setFacet(FacetTypes facetType) throws IOException {
        definedFacets.add(facetType);
        switch (facetType) {
            case LENGTH:
                checkField(this.length, "length");
                this.length = getIntegerFromObject();
                break;
            case MIN_LENGTH:
                checkField(this.minLength, "maxLength");
                this.minLength = getIntegerFromObject();
                break;
            case MAX_LENGTH:
                checkField(this.maxLength, "minLength");
                this.maxLength = getIntegerFromObject();
                break;
            case MIN_INCLUSIVE:
                checkField(this.minInclusive, "minInclusive");
                this.minInclusive = getStringFromObject();
                break;
            case MAX_INCLUSIVE:
                checkField(this.maxInclusive, "maxInclusive");
                this.maxInclusive = getStringFromObject();
                break;
            case MIN_EXCLUSIVE:
                checkField(this.minExclusive, "minExclusive");
                this.minExclusive = getStringFromObject();
                break;
            case MAX_EXCLUSIVE:
                checkField(this.maxExclusive, "maxExclusive");
                this.maxExclusive = getStringFromObject();
                break;
            case TOTAL_DIGITS:
                checkField(this.totalDigits, "totalDigits");
                this.totalDigits = getIntegerFromObject();
                break;
            case FRACTION_DIGITS:
                checkField(this.fractionDigits, "fractionDigits");
                this.fractionDigits = getIntegerFromObject();
                break;
            case EXPLICIT_TIMEZONE:
                checkField(this.explicitTimezone, "explicitTimezone");
                this.explicitTimezone = TimezoneFacet.valueOf(getStringFromObject().toUpperCase());
                break;
            case ENUMERATION:
            case METADATA:
            case CONSTRAINTS:
                super.setFacet(facetType);
        }
    }

    protected static Integer getIntegerFromObject() throws IOException {
        if (!object.whatIsNext().equals(ValueType.NUMBER))
            throw new UnexpectedTypeException("Invalid number " + object.read().toString());
        return object.readInt();
    }
}
