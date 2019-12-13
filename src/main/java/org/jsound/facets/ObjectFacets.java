package org.jsound.facets;

import com.jsoniter.ValueType;
import jsound.exceptions.InvalidSchemaException;
import org.jsound.json.SchemaFileJsonParser;
import org.jsound.type.FieldDescriptor;
import org.jsound.type.TypeOrReference;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.jsound.cli.JSoundExecutor.object;
import static org.jsound.cli.JSoundExecutor.schema;
import static org.jsound.json.InstanceFileJsonParser.getItemFromObject;

public class ObjectFacets extends Facets {

    public Map<String, FieldDescriptor> content = null;
    private Boolean closed = null;
    private Boolean hasDefaultValue = false;

    @Override
    public void setFacet(FacetTypes facetType) throws IOException {
        definedFacets.add(facetType);
        switch (facetType) {
            case CONTENT:
                checkField(this.content, "objectContent");
                this.setObjectContentFromObject();
                break;
            case CLOSED:
                checkField(this.closed, "closed");
                this.closed = Boolean.parseBoolean(getStringFromObject());
                break;
            case ENUMERATION:
            case METADATA:
            case CONSTRAINTS:
                super.setFacet(facetType);
        }
    }

    private void setObjectContentFromObject() throws IOException {
        String key;
        Map<String, FieldDescriptor> fieldDescriptors = new LinkedHashMap<>();
        while (object.readArray()) {
            FieldDescriptor fieldDescriptor = new FieldDescriptor();
            while ((key = object.readObject()) != null) {
                switch (key) {
                    case "name":
                        String name = getStringFromObject();
                        if (fieldDescriptors.containsKey(name))
                            throw new InvalidSchemaException("The field descriptor " + name + " was already defined.");
                        fieldDescriptor.setName(name);
                        break;
                    case "atomicTypes":
                        setFieldDescriptorType(fieldDescriptor);
                        break;
                    case "required":
                        fieldDescriptor.setRequired(Boolean.parseBoolean(getStringFromObject()));
                        break;
                    case "unique":
                        fieldDescriptor.setUnique(Boolean.parseBoolean(getStringFromObject()));
                        break;
                    case "default":
                        fieldDescriptor.setDefaultValue(getItemFromObject(object));
                        this.hasDefaultValue = true;
                        break;
                    default:
                        throw new InvalidSchemaException(key + " is not a valid property for the field descriptor.");
                }
            }
            fieldDescriptors.put(fieldDescriptor.getName(), fieldDescriptor);
        }
        this.content = fieldDescriptors;
    }

    private static void setFieldDescriptorType(FieldDescriptor fieldDescriptor) throws IOException {
        if (object.whatIsNext().equals(ValueType.STRING)) {
            String fieldType = object.readString();
            if (schema.containsKey(fieldType))
                fieldDescriptor.setType(new TypeOrReference(schema.get(fieldType)));
            else
                fieldDescriptor.setType(new TypeOrReference(fieldType));
        } else if (!object.whatIsNext().equals(ValueType.OBJECT))
            throw new InvalidSchemaException("Type for field descriptors must be either string or object.");
        else
            fieldDescriptor.setType(new TypeOrReference(SchemaFileJsonParser.getTypeDescriptor()));
    }

    public Boolean hasDefaultValue() {
        return hasDefaultValue;
    }
}
