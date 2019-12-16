package org.jsound.facets;

import com.jsoniter.ValueType;
import org.jsound.json.SchemaFileJsonParser;
import org.jsound.type.TypeOrReference;
import org.jsound.type.UnionContentDescriptor;

import java.io.IOException;

import static org.jsound.cli.JSoundExecutor.object;
import static org.jsound.cli.JSoundExecutor.schema;
import static org.jsound.json.CompactSchemaFileJsonParser.compactSchema;

public class UnionFacets extends Facets {
    private UnionContentDescriptor unionContent;

    public UnionFacets() {
        unionContent = new UnionContentDescriptor();
    }

    @Override
    public void setFacet(FacetTypes facetType) throws IOException {
        definedFacets.add(facetType);
        switch (facetType) {
            case CONTENT:
                checkField(this.unionContent, "unionContent");
                this.setUnionContentFromObject();
                break;
            case ENUMERATION:
            case METADATA:
            case CONSTRAINTS:
                super.setFacet(facetType);
        }
    }

    private void setUnionContentFromObject() throws IOException {
        UnionContentDescriptor unionContent = new UnionContentDescriptor();
        while (object.readArray()) {
            if (object.whatIsNext().equals(ValueType.STRING)) {
                String type = object.readString();
                if (schema.containsKey(type))
                    unionContent.getTypes().add(new TypeOrReference(schema.get(type)));
                else
                    unionContent.getTypes().add(new TypeOrReference(type));
            } else
                unionContent.getTypes().add(new TypeOrReference(SchemaFileJsonParser.getTypeDescriptor(true)));
        }
        this.unionContent = unionContent;
    }

    public void setUnionContent(String unionContentString) {
        String[] unionTypes = unionContentString.split("\\|");
        UnionContentDescriptor unionContent = new UnionContentDescriptor();
        for (String type : unionTypes) {
            if (compactSchema.containsKey(type))
                unionContent.getTypes().add(compactSchema.get(type));
            else
                unionContent.getTypes().add(new TypeOrReference(type));
        }
        this.unionContent = unionContent;
    }

    @Override
    public UnionContentDescriptor getUnionContent() {
        return unionContent;
    }
}
