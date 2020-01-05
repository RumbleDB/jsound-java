package jsound.facets;

import com.jsoniter.ValueType;
import jsound.exceptions.InvalidSchemaException;
import jsound.json.SchemaFileJsonParser;
import jsound.typedescriptors.TypeOrReference;
import jsound.typedescriptors.array.ArrayContentDescriptor;
import org.api.TypeDescriptor;

import java.io.IOException;

import static jsound.facets.FacetTypes.CONTENT;
import static jsound.json.CompactSchemaFileJsonParser.compactSchema;
import static jsound.json.CompactSchemaFileJsonParser.getTypeFromObject;
import static jsound.json.CompactSchemaFileJsonParser.resolveType;
import static org.api.executors.JSoundExecutor.jsonSchemaIterator;
import static org.api.executors.JSoundExecutor.schema;

public class ArrayFacets extends Facets {
    public ArrayContentDescriptor arrayContent =
        new ArrayContentDescriptor(
                new TypeOrReference(TypeDescriptor.getValueInstance())
        );
    public Integer minLength = null, maxLength = null;

    @Override
    public void setFacet(FacetTypes facetType, String typeName) throws IOException {
        checkField(facetType);
        switch (facetType) {
            case CONTENT:
                this.setArrayContentFromObject();
                break;
            case MIN_LENGTH:
                this.minLength = AtomicFacets.getIntegerFromObject();
                break;
            case MAX_LENGTH:
                this.maxLength = AtomicFacets.getIntegerFromObject();
                break;
            case ENUMERATION:
            case METADATA:
            case CONSTRAINTS:
                super.setFacet(facetType, typeName);
        }
        definedFacets.add(facetType);
    }

    public void setArrayContentFromObject() throws IOException {
        int size = 0;
        while (jsonSchemaIterator.readArray()) {
            if (size > 0)
                throw new InvalidSchemaException("Can only specify one atomicTypes for the array content atomicTypes.");
            if (jsonSchemaIterator.whatIsNext().equals(ValueType.STRING)) {
                String contentType = jsonSchemaIterator.readString();
                if (schema.containsKey(contentType))
                    this.arrayContent = new ArrayContentDescriptor(
                            new TypeOrReference(schema.get(contentType))
                    );
                else
                    this.arrayContent = new ArrayContentDescriptor(
                            new TypeOrReference(contentType)
                    );
            }
            size++;
        }
        if (size == 0)
            throw new InvalidSchemaException("You must specify the content atomicTypes for array.");
        this.arrayContent = new ArrayContentDescriptor(
                new TypeOrReference(SchemaFileJsonParser.getTypeDescriptor(true))
        );
    }

    public void setArrayContent(String name) throws IOException {
        definedFacets.add(CONTENT);
        int size = 0;
        while (jsonSchemaIterator.readArray()) {
            if (size > 0)
                throw new InvalidSchemaException("Can only specify one content type for array type " + name + ".");
            if (jsonSchemaIterator.whatIsNext().equals(ValueType.STRING)) {
                String contentType = jsonSchemaIterator.readString();
                this.arrayContent = new ArrayContentDescriptor(
                        resolveType(contentType)
                );
            }
            size++;
        }
        if (size == 0)
            throw new InvalidSchemaException("You must specify the content type for array for type " + name + ".");
        if (arrayContent == null)
            arrayContent = new ArrayContentDescriptor(getTypeFromObject(name));
    }

    @Override
    public ArrayContentDescriptor getArrayContent() {
        return arrayContent;
    }
}
