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
    public ArrayContentDescriptor arrayContent = null;
    public Integer minLength = null, maxLength = null;

    @Override
    public void setFacet(FacetTypes facetType, String typeName) throws IOException {
        definedFacets.add(facetType);
        switch (facetType) {
            case CONTENT:
                checkField(this.arrayContent, "arrayContent");
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
                super.setFacet(facetType, typeName);
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
                    this.arrayContent = new ArrayContentDescriptor(new TypeOrReference(schema.get(contentType)));
                else
                    this.arrayContent = new ArrayContentDescriptor(new TypeOrReference(contentType));
            }
            size++;
        }
        if (size == 0)
            throw new InvalidSchemaException("You must specify the content atomicTypes for array.");
        if (this.arrayContent == null)
            this.arrayContent = new ArrayContentDescriptor(
                    new TypeOrReference(SchemaFileJsonParser.getTypeDescriptor(true))
            );
    }

    public void setArrayContent(String name) throws IOException {
        definedFacets.add(FacetTypes.CONTENT);
        int size = 0;
        while (object.readArray()) {
            if (size > 0)
                throw new InvalidSchemaException("Can only specify one atomicTypes for the array content atomicTypes.");
            if (object.whatIsNext().equals(ValueType.STRING)) {
                String contentType = object.readString();
                if (schema.containsKey(contentType))
                    this.arrayContent = new ArrayContentDescriptor(compactSchema.get(contentType));
                else
                    this.arrayContent = new ArrayContentDescriptor(new TypeOrReference(contentType));
            }
            size++;
        }
        if (size == 0)
            throw new InvalidSchemaException("You must specify the content atomicTypes for array.");
        if (arrayContent == null)
            arrayContent = new ArrayContentDescriptor(getTypeFromObject(name));
    }

    @Override
    public ArrayContentDescriptor getArrayContent() {
        return arrayContent;
    }
}
