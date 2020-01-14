package jsound.facets;

import com.jsoniter.ValueType;
import jsound.exceptions.UnexpectedTypeException;
import org.api.ItemWrapper;

import java.io.IOException;

import static jsound.json.InstanceFileJsonParser.getItemFromObject;
import static org.api.executors.JSoundExecutor.jsonSchemaIterator;

public class AtomicFacets extends Facets {
    public Integer length = null, minLength = null, maxLength = null;
    public ItemWrapper minInclusive = null, maxInclusive = null, minExclusive = null, maxExclusive = null;
    public Integer totalDigits = null, fractionDigits = null;
    public jsound.facets.TimezoneFacet explicitTimezone = null;

    @Override
    public void setFacet(FacetTypes facetType, String typeName) throws IOException {
        checkField(facetType);
        switch (facetType) {
            case LENGTH:
                this.length = getIntegerFromObject();
                break;
            case MINLENGTH:
                this.minLength = getIntegerFromObject();
                break;
            case MAXLENGTH:
                this.maxLength = getIntegerFromObject();
                break;
            case MININCLUSIVE:
                this.minInclusive = getItemFromObject(jsonSchemaIterator);
                break;
            case MAXINCLUSIVE:
                this.maxInclusive = getItemFromObject(jsonSchemaIterator);
                break;
            case MINEXCLUSIVE:
                this.minExclusive = getItemFromObject(jsonSchemaIterator);
                break;
            case MAXEXCLUSIVE:
                this.maxExclusive = getItemFromObject(jsonSchemaIterator);
                break;
            case TOTALDIGITS:
                this.totalDigits = getIntegerFromObject();
                break;
            case FRACTIONDIGITS:
                this.fractionDigits = getIntegerFromObject();
                break;
            case EXPLICITTIMEZONE:
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
            throw new UnexpectedTypeException("Invalid number " + jsonSchemaIterator.read());
        return jsonSchemaIterator.readInt();
    }
}
