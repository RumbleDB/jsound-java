package org.jsound.facets;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import jsound.exceptions.UnexpectedTypeException;
import org.jsound.api.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.jsound.json.InstanceFileJsonParser.getItemFromObject;
import static org.jsound.json.SchemaFileJsonParser.*;

public enum FacetTypes {
    LENGTH {
        @Override
        public void setFacet(Facets facets, JsonIterator object) throws IOException {
            checkField(facets.length, "length");
            facets.length = getIntegerFromObject(object);
        }
    },
    MIN_LENGTH {
        @Override
        public void setFacet(Facets facets, JsonIterator object) throws IOException {
            checkField(facets.minLength, "minLength");
            facets.minLength = getIntegerFromObject(object);
        }
    },
    MAX_LENGTH {
        @Override
        public void setFacet(Facets facets, JsonIterator object) throws IOException {
            checkField(facets.maxLength, "maxLength");
            facets.maxLength = getIntegerFromObject(object);
        }
    },
    MIN_INCLUSIVE {
        @Override
        public void setFacet(Facets facets, JsonIterator object) throws IOException {
            checkField(facets.minInclusive, "minInclusive");
            facets.minInclusive = getStringFromObject(object);
        }
    },
    MAX_INCLUSIVE {
        @Override
        public void setFacet(Facets facets, JsonIterator object) throws IOException {
            checkField(facets.maxInclusive, "maxInclusive");
            facets.maxInclusive = getStringFromObject(object);
        }
    },
    MIN_EXCLUSIVE {
        @Override
        public void setFacet(Facets facets, JsonIterator object) throws IOException {
            checkField(facets.minExclusive, "minExclusive");
            facets.minExclusive = getStringFromObject(object);
        }
    },
    MAX_EXCLUSIVE {
        @Override
        public void setFacet(Facets facets, JsonIterator object) throws IOException {
            checkField(facets.maxExclusive, "maxExclusive");
            facets.maxExclusive = getStringFromObject(object);
        }
    },
    TOTAL_DIGITS {
        @Override
        public void setFacet(Facets facets, JsonIterator object) throws IOException {
            checkField(facets.totalDigits, "totalDigits");
            facets.totalDigits = getIntegerFromObject(object);
        }
    },
    FRACTION_DIGITS {
        @Override
        public void setFacet(Facets facets, JsonIterator object) throws IOException {
            checkField(facets.fractionDigits, "fractionDigits");
            facets.fractionDigits = getIntegerFromObject(object);
        }
    },
    ENUMERATION {
        @Override
        public void setFacet(Facets facets, JsonIterator object) throws IOException {
            checkField(facets.enumeration, "enumeration");
            facets.enumeration = getEnumerationFromObject(object);
        }
    },
    METADATA {
        @Override
        public void setFacet(Facets facets, JsonIterator object) {
            checkField(facets.metadata, "metadata");
            facets.metadata = getItemFromObject(object);
        }
    },
    CONSTRAINTS {
        @Override
        public void setFacet(Facets facets, JsonIterator object) throws IOException {
            checkField(facets.constraints, "maxLength");
            facets.constraints = getConstraintsTypeFromObject(object);
        }
    };

    public void setFacet(Facets facets, JsonIterator object) throws IOException {
    }

    private static List<Item> getEnumerationFromObject(JsonIterator object) throws IOException {
        if (!object.whatIsNext().equals(ValueType.ARRAY))
            throw new UnexpectedTypeException("Enumeration should be an array.");
        List<Item> enumerationItemTypes = new ArrayList<>();
        while (object.readArray()) {
            enumerationItemTypes.add(getItemFromObject(object));
        }
        return enumerationItemTypes;
    }

}
