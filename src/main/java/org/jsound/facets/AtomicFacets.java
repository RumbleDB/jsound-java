package org.jsound.facets;

import com.jsoniter.ValueType;
import jsound.exceptions.UnexpectedTypeException;
import org.jsound.item.Item;

import java.io.IOException;

import static org.jsound.cli.JSoundExecutor.object;
import static org.jsound.json.InstanceFileJsonParser.getItemFromObject;

public class AtomicFacets extends Facets {
    public Integer length = null, minLength = null, maxLength = null;
    public Item minInclusive = null, maxInclusive = null, minExclusive = null, maxExclusive = null;
    public Integer totalDigits = null, fractionDigits = null;
    public TimezoneFacet explicitTimezone = null;

    @Override
    public void setFacet(FacetTypes facetType, String typeName) throws IOException {
        definedFacets.add(facetType);
        switch (facetType) {
            case LENGTH:
                checkField(this.length, "length");
                this.length = getIntegerFromObject();
                break;
            case MIN_LENGTH:
                checkField(this.minLength, "minLength");
                this.minLength = getIntegerFromObject();
                break;
            case MAX_LENGTH:
                checkField(this.maxLength, "maxLength");
                this.maxLength = getIntegerFromObject();
                break;
            case MIN_INCLUSIVE:
                checkField(this.minInclusive, "minInclusive");
                this.minInclusive = getItemFromObject(object);
                break;
            case MAX_INCLUSIVE:
                checkField(this.maxInclusive, "maxInclusive");
                this.maxInclusive = getItemFromObject(object);
                break;
            case MIN_EXCLUSIVE:
                checkField(this.minExclusive, "minExclusive");
                this.minExclusive = getItemFromObject(object);
                break;
            case MAX_EXCLUSIVE:
                checkField(this.maxExclusive, "maxExclusive");
                this.maxExclusive = getItemFromObject(object);
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
                this.explicitTimezone = TimezoneFacet.valueOf(getStringFromObject("explicitTimezone").toUpperCase());
                break;
            case ENUMERATION:
            case METADATA:
            case CONSTRAINTS:
                super.setFacet(facetType, typeName);
        }
    }

    protected static Integer getIntegerFromObject() throws IOException {
        if (!object.whatIsNext().equals(ValueType.NUMBER))
            throw new UnexpectedTypeException("Invalid number " + object.read().toString());
        return object.readInt();
    }
}
