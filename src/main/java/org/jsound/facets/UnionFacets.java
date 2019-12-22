package org.jsound.facets;

import com.jsoniter.ValueType;
import org.jsound.json.SchemaFileJsonParser;
import org.jsound.typedescriptors.TypeOrReference;
import org.jsound.typedescriptors.union.UnionContentDescriptor;

import java.io.IOException;

import static org.jsound.cli.JSoundExecutor.jsonSchemaIterator;
import static org.jsound.cli.JSoundExecutor.schema;
import static org.jsound.json.CompactSchemaFileJsonParser.compactSchema;

public class UnionFacets extends Facets {
    private UnionContentDescriptor unionContent = new UnionContentDescriptor();

    @Override
    public void setFacet(FacetTypes facetType, String typeName) throws IOException {
        checkField(facetType);
        switch (facetType) {
            case CONTENT:
                this.setUnionContentFromObject();
                break;
            case ENUMERATION:
            case METADATA:
            case CONSTRAINTS:
                super.setFacet(facetType, typeName);
        }
        definedFacets.add(facetType);
    }

    private void setUnionContentFromObject() throws IOException {
        while (jsonSchemaIterator.readArray()) {
            if (jsonSchemaIterator.whatIsNext().equals(ValueType.STRING)) {
                String type = jsonSchemaIterator.readString();
                if (schema.containsKey(type))
                    unionContent.getTypes().add(new TypeOrReference(schema.get(type)));
                else
                    unionContent.getTypes().add(new TypeOrReference(type));
            } else
                unionContent.getTypes().add(new TypeOrReference(SchemaFileJsonParser.getTypeDescriptor(true)));
        }
    }

    public void setUnionContent(String unionContentString) {
        String[] unionTypes = unionContentString.split("\\|");
        for (String type : unionTypes) {
            if (compactSchema.containsKey(type))
                unionContent.getTypes().add(compactSchema.get(type));
            else
                unionContent.getTypes().add(new TypeOrReference(type));
        }
    }

    @Override
    public UnionContentDescriptor getUnionContent() {
        return unionContent;
    }
}
