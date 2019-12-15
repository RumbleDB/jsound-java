package org.jsound.facets;

import com.jsoniter.ValueType;
import jsound.exceptions.InvalidSchemaException;
import org.jsound.json.SchemaFileJsonParser;
import org.jsound.type.ArrayContentDescriptor;
import org.jsound.type.TypeOrReference;

import java.io.IOException;

import static org.jsound.cli.JSoundExecutor.object;
import static org.jsound.cli.JSoundExecutor.schema;
import static org.jsound.facets.AtomicFacets.getIntegerFromObject;
import static org.jsound.json.CompactSchemaFileJsonParser.compactSchema;
import static org.jsound.json.CompactSchemaFileJsonParser.getTypeFromObject;

public class ArrayFacets extends Facets {
    public ArrayContentDescriptor content = null;
    public Integer minLength = null, maxLength = null;

    @Override
    public void setFacet(FacetTypes facetType) throws IOException {
        definedFacets.add(facetType);
        switch (facetType) {
            case CONTENT:
                checkField(this.content, "arrayContent");
                this.setArrayContentFromObject();
                break;
            case MIN_LENGTH:
                checkField(this.minLength, "minLength");
                this.minLength = getIntegerFromObject();
                break;
            case MAX_LENGTH:
                checkField(this.maxLength, "maxLength");
                this.maxLength = getIntegerFromObject();
                break;
            case ENUMERATION:
            case METADATA:
            case CONSTRAINTS:
                super.setFacet(facetType);
        }
    }

    public void setArrayContentFromObject() throws IOException {
        int size = 0;
        while (object.readArray()) {
            if (size > 0)
                throw new InvalidSchemaException("Can only specify one atomicTypes for the array content atomicTypes.");
            if (object.whatIsNext().equals(ValueType.STRING)) {
                String contentType = object.readString();
                if (schema.containsKey(contentType))
                    this.content = new ArrayContentDescriptor(new TypeOrReference(schema.get(contentType)));
                else
                    this.content = new ArrayContentDescriptor(new TypeOrReference(contentType));
            }
            size++;
        }
        if (size == 0)
            throw new InvalidSchemaException("You must specify the content atomicTypes for array.");
        if (this.content == null)
            this.content = new ArrayContentDescriptor(
                    new TypeOrReference(SchemaFileJsonParser.getTypeDescriptor(true))
            );
    }

    public void setContent(String name) throws IOException {
        int size = 0;
        while (object.readArray()) {
            if (size > 0)
                throw new InvalidSchemaException("Can only specify one atomicTypes for the array content atomicTypes.");
            if (object.whatIsNext().equals(ValueType.STRING)) {
                String contentType = object.readString();
                if (schema.containsKey(contentType))
                    this.content = new ArrayContentDescriptor(compactSchema.get(contentType));
                else
                    this.content = new ArrayContentDescriptor(new TypeOrReference(contentType));
            }
            size++;
        }
        if (size == 0)
            throw new InvalidSchemaException("You must specify the content atomicTypes for array.");
        if (content == null)
            content = new ArrayContentDescriptor(getTypeFromObject(name));
    }
}
