package jsound.facets;

import com.jsoniter.ValueType;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.MissingNameOrTypeException;
import jsound.exceptions.UnexpectedTypeException;
import jsound.json.SchemaFileJsonParser;
import jsound.typedescriptors.TypeOrReference;
import jsound.typedescriptors.object.FieldDescriptor;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static jsound.json.InstanceFileJsonParser.getItemFromObject;
import static org.api.executors.JSoundExecutor.jsonSchemaIterator;
import static org.api.executors.JSoundExecutor.schema;

public class ObjectFacets extends Facets {

    private Map<String, FieldDescriptor> objectContent = new LinkedHashMap<>();
    private boolean closed = false;
    public boolean closedIsSet = false;

    @Override
    public void setFacet(FacetTypes facetType, String typeName) throws IOException {
        checkField(facetType);
        switch (facetType) {
            case CONTENT:
                this.setObjectContentFromObject(typeName);
                break;
            case CLOSED:
                this.closed = getBooleanFromObject();
                this.closedIsSet = true;
                break;
            case ENUMERATION:
            case METADATA:
            case CONSTRAINTS:
                super.setFacet(facetType, typeName);
        }
        definedFacets.add(facetType);
    }

    private void setObjectContentFromObject(String typeName) throws IOException {
        String key;
        while (jsonSchemaIterator.readArray()) {
            FieldDescriptor fieldDescriptor = new FieldDescriptor();
            while ((key = jsonSchemaIterator.readObject()) != null) {
                switch (key) {
                    case "name":
                        String name = getStringFromObject("name");
                        if (objectContent.containsKey(name))
                            throw new InvalidSchemaException("The field descriptor " + name + " was already defined.");
                        fieldDescriptor.setName(name);
                        break;
                    case "type":
                        setFieldDescriptorType(fieldDescriptor);
                        break;
                    case "required":
                        fieldDescriptor.setRequired(getBooleanFromObject());
                        break;
                    case "unique":
                        fieldDescriptor.setUnique(getBooleanFromObject());
                        break;
                    case "default":
                        fieldDescriptor.setDefaultValue(getItemFromObject(jsonSchemaIterator));
                        break;
                    default:
                        throw new InvalidSchemaException(key + " is not a valid property for the field descriptor.");
                }
            }
            if (fieldDescriptor.getName() == null) {
                throw new MissingNameOrTypeException(
                        "Field \"name\" is missing in object content for type " + typeName
                );
            }
            objectContent.put(fieldDescriptor.getName(), fieldDescriptor);
        }
    }

    private static void setFieldDescriptorType(FieldDescriptor fieldDescriptor) throws IOException {
        if (jsonSchemaIterator.whatIsNext().equals(ValueType.STRING)) {
            String fieldType = jsonSchemaIterator.readString();
            if (schema.containsKey(fieldType))
                fieldDescriptor.setType(new TypeOrReference(schema.get(fieldType)));
            else
                fieldDescriptor.setType(new TypeOrReference(fieldType));
        } else if (!jsonSchemaIterator.whatIsNext().equals(ValueType.OBJECT))
            throw new InvalidSchemaException("Type for field descriptors must be either string or object.");
        else
            fieldDescriptor.setType(new TypeOrReference(SchemaFileJsonParser.getTypeDescriptor(true)));
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    @Override
    public Map<String, FieldDescriptor> getObjectContent() {
        return objectContent;
    }

    public boolean isClosed() {
        return closed;
    }

    public static boolean getBooleanFromObject() throws IOException {
        if (!jsonSchemaIterator.whatIsNext().equals(ValueType.BOOLEAN))
            throw new UnexpectedTypeException("Invalid string " + jsonSchemaIterator.read().toString());
        return jsonSchemaIterator.readBoolean();
    }
}
