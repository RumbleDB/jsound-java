package jsound.json;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import jsound.atomicTypes.NullType;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.JsoundException;
import jsound.exceptions.UnexpectedTypeException;
import jsound.facets.ArrayFacets;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import jsound.facets.ObjectFacets;
import jsound.facets.UnionFacets;
import jsound.item.ItemFactory;
import jsound.typedescriptors.TypeOrReference;
import jsound.typedescriptors.array.ArrayTypeDescriptor;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.typedescriptors.object.FieldDescriptor;
import jsound.typedescriptors.object.ObjectTypeDescriptor;
import jsound.typedescriptors.union.UnionTypeDescriptor;
import jsound.types.AtomicTypes;
import org.api.ItemWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.api.executors.JSoundExecutor.jsonSchemaIterator;
import static org.api.executors.JSoundExecutor.schema;


public class CompactSchemaFileJsonParser {

    public static Map<String, TypeOrReference> compactSchema = new HashMap<>();

    public static void createSchema() {

        try {
            if (!jsonSchemaIterator.whatIsNext().equals(ValueType.OBJECT)) {
                throw new InvalidSchemaException("The schema root object must be a JSON object");
            }

            String typeName;
            while ((typeName = jsonSchemaIterator.readObject()) != null) {
                if (compactSchema.containsKey(typeName))
                    SchemaFileJsonParser.throwExistingTypeException(typeName);
                compactSchema.put(typeName, getTypeFromObject(typeName));
            }
            for (String key : compactSchema.keySet()) {
                if (schema.get(key) == null)
                    SchemaDefinitionUtils.resolveTypeDescriptors(key);
            }
        } catch (IOException e) {
            throw new JsoundException("Error parsing the JSON file");
        }
    }

    public static TypeOrReference getTypeFromObject(String name) {
        try {
            switch (jsonSchemaIterator.whatIsNext()) {
                case STRING:
                    return parseType(name, jsonSchemaIterator.readString());
                case OBJECT:
                    return new TypeOrReference(buildObjectType(name, new ObjectFacets()));
                case ARRAY:
                    ArrayFacets facets = new ArrayFacets();
                    facets.setArrayContent(name);
                    return new TypeOrReference(new ArrayTypeDescriptor(name, facets));
                default:
                    throw new UnexpectedTypeException("Type for " + name + " is not string nor object nor array.");
            }
        } catch (IOException e) {
            throw new JsoundException("Error parsing the JSON file");
        }
    }

    private static ObjectTypeDescriptor buildObjectType(String name, ObjectFacets facets) throws IOException {
        String key;
        while ((key = jsonSchemaIterator.readObject()) != null) {
            FieldDescriptor fieldDescriptor = new FieldDescriptor();
            boolean allowNull = setMarkers(fieldDescriptor, key);
            if (facets.getObjectContent().containsKey(fieldDescriptor.getName()))
                throw new InvalidSchemaException("The field descriptor " + name + " was already defined.");
            setFieldDescriptorType(fieldDescriptor);
            if (allowNull) {
                UnionFacets unionTypeFacets = new UnionFacets();
                if (fieldDescriptor.getTypeOrReference().getStringType() != null)
                    unionTypeFacets.getUnionContent()
                        .getTypes()
                        .add(new TypeOrReference(fieldDescriptor.getTypeOrReference().getStringType()));
                else
                    unionTypeFacets.getUnionContent()
                        .getTypes()
                        .add(new TypeOrReference(fieldDescriptor.getTypeOrReference().getType()));
                unionTypeFacets.getUnionContent()
                    .getTypes()
                    .add(new TypeOrReference(new NullType(null, new AtomicFacets())));
                unionTypeFacets.definedFacets.add(FacetTypes.CONTENT);
                fieldDescriptor.setType(new TypeOrReference(new UnionTypeDescriptor(name, unionTypeFacets)));
            }
            facets.getObjectContent().put(fieldDescriptor.getName(), fieldDescriptor);
        }
        facets.getDefinedFacets().add(FacetTypes.CONTENT);
        return new ObjectTypeDescriptor(name, facets);
    }

    private static void setFieldDescriptorType(FieldDescriptor fieldDescriptor) throws IOException {
        if (jsonSchemaIterator.whatIsNext().equals(ValueType.STRING)) {
            String fieldValue = jsonSchemaIterator.readString();
            String fieldType = fieldValue.split("=", 2)[0];
            if (fieldValue.contains("=")) {
                String defaultValue = fieldValue.split("=", 2)[1];
                if (defaultValue.startsWith("{") || defaultValue.startsWith("[")) {
                    try {
                        fieldDescriptor.setDefaultValue(
                            InstanceFileJsonParser.getItemFromObject(JsonIterator.parse(defaultValue))
                        );
                    } catch (JsoundException e) {
                        fieldDescriptor.setDefaultValue(
                            new ItemWrapper(
                                    ItemFactory.getInstance().createStringItem(defaultValue)
                            )
                        );
                    }
                } else {
                    fieldDescriptor.setDefaultValue(
                        new ItemWrapper(
                                ItemFactory.getInstance().createStringItem(defaultValue)
                        )
                    );
                }
            }
            if (compactSchema.containsKey(fieldType))
                fieldDescriptor.setType(compactSchema.get(fieldType));
            else
                fieldDescriptor.setType(parseType(fieldDescriptor.name, fieldType));
        } else if (!jsonSchemaIterator.whatIsNext().equals(ValueType.OBJECT))
            throw new InvalidSchemaException("Type for field descriptors must be either string or object.");
        else
            fieldDescriptor.setType(getTypeFromObject(fieldDescriptor.name));
    }

    private static boolean setMarkers(FieldDescriptor fieldDescriptor, String name) {
        fieldDescriptor.setRequired(name.contains("!"));
        fieldDescriptor.setUnique(name.contains("@"));
        boolean allowNull = name.contains("?");
        fieldDescriptor.setName(
            name
                .replace("!", "")
                .replace("@", "")
                .replace("?", "")
        );
        return allowNull;
    }

    private static TypeOrReference parseType(String name, String typeString) throws IOException {
        if (typeString.contains("|")) {
            UnionFacets facets = new UnionFacets();
            facets.setUnionContent(typeString);
            facets.definedFacets.add(FacetTypes.CONTENT);
            return new TypeOrReference(new UnionTypeDescriptor(name, facets));
        }
        try {
            return new TypeOrReference(
                    AtomicTypeDescriptor.buildAtomicType(AtomicTypes.valueOf(typeString.toUpperCase()), name, false)
            );
        } catch (IllegalArgumentException e) {
            if (compactSchema.containsKey(typeString))
                return compactSchema.get(typeString);
            return new TypeOrReference(typeString);
        }
    }
}
