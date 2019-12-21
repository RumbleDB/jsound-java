package org.jsound.facets;

import com.jsoniter.ValueType;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.MissingNameOrTypeException;
import jsound.exceptions.UnexpectedTypeException;
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

    private Map<String, FieldDescriptor> objectContent = new LinkedHashMap<>();
    private Boolean closed = null;
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
        while (object.readArray()) {
            FieldDescriptor fieldDescriptor = new FieldDescriptor();
            while ((key = object.readObject()) != null) {
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
                        fieldDescriptor.setDefaultValue(getItemFromObject(object));
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
        if (object.whatIsNext().equals(ValueType.STRING)) {
            String fieldType = object.readString();
            if (schema.containsKey(fieldType))
                fieldDescriptor.setType(new TypeOrReference(schema.get(fieldType)));
            else
                fieldDescriptor.setType(new TypeOrReference(fieldType));
        } else if (!object.whatIsNext().equals(ValueType.OBJECT))
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

    @Override
    public boolean isClosed() {
        return closed != null ? closed : false;
    }

    public static boolean getBooleanFromObject() throws IOException {
        if (!object.whatIsNext().equals(ValueType.BOOLEAN))
            throw new UnexpectedTypeException("Invalid string " + object.read().toString());
        return object.readBoolean();
    }
}
