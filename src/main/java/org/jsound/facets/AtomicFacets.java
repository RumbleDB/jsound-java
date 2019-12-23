package org.jsound.facets;

import com.jsoniter.ValueType;
import jsound.exceptions.UnexpectedTypeException;
import org.jsound.item.Item;

import java.io.IOException;

import static org.jsound.cli.JSoundExecutor.jsonSchemaIterator;
import static org.jsound.json.InstanceFileJsonParser.getItemFromObject;

public class AtomicFacets extends Facets {
    public Integer length = null, minLength = null, maxLength = null;
    public Item minInclusive = null, maxInclusive = null, minExclusive = null, maxExclusive = null;
    public Integer totalDigits = null, fractionDigits = null;
    public TimezoneFacet explicitTimezone = null;

    @Override
    public void setFacet(FacetTypes facetType, String typeName) throws IOException {
        checkField(facetType);
        switch (facetType) {
            case LENGTH:
                this.length = getIntegerFromObject();
                break;
            case MIN_LENGTH:
                this.minLength = getIntegerFromObject();
                break;
            case MAX_LENGTH:
                this.maxLength = getIntegerFromObject();
                break;
            case MIN_INCLUSIVE:
                this.minInclusive = getItemFromObject(jsonSchemaIterator);
                break;
            case MAX_INCLUSIVE:
                this.maxInclusive = getItemFromObject(jsonSchemaIterator);
                break;
            case MIN_EXCLUSIVE:
                this.minExclusive = getItemFromObject(jsonSchemaIterator);
                break;
            case MAX_EXCLUSIVE:
                this.maxExclusive = getItemFromObject(jsonSchemaIterator);
                break;
            case TOTAL_DIGITS:
                this.totalDigits = getIntegerFromObject();
                break;
            case FRACTION_DIGITS:
                this.fractionDigits = getIntegerFromObject();
                break;
            case EXPLICIT_TIMEZONE:
                this.explicitTimezone = TimezoneFacet.valueOf(
                    getStringFromObject(facetType.getTypeName()).toUpperCase()
                );
                break;
            case ENUMERATION:
            case METADATA:
            case CONSTRAINTS:
                super.setFacet(facetType, typeName);
        }
        definedFacets.add(facetType);
    }

    protected static Integer getIntegerFromObject() throws IOException {
        if (!jsonSchemaIterator.whatIsNext().equals(ValueType.NUMBER))
            throw new UnexpectedTypeException("Invalid number " + jsonSchemaIterator.read().toString());
        return jsonSchemaIterator.readInt();
    }
}
