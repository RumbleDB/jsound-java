package org.jsound.json;

import com.jsoniter.ValueType;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.JsoundException;
import jsound.exceptions.UnexpectedTypeException;
import org.jsound.atomicTypes.NullType;
import org.jsound.facets.ArrayFacets;
import org.jsound.facets.AtomicFacets;
import org.jsound.facets.ObjectFacets;
import org.jsound.facets.UnionFacets;
import org.jsound.item.ItemFactory;
import org.jsound.type.ArrayTypeDescriptor;
import org.jsound.type.AtomicTypes;
import org.jsound.type.FieldDescriptor;
import org.jsound.type.ObjectTypeDescriptor;
import org.jsound.type.TypeDescriptor;
import org.jsound.type.TypeOrReference;
import org.jsound.type.UnionTypeDescriptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.jsound.cli.JSoundExecutor.object;
import static org.jsound.cli.JSoundExecutor.schema;
import static org.jsound.type.AtomicTypeDescriptor.buildAtomicType;


public class CompactSchemaFileJsonParser {

    public static Map<String, TypeOrReference> compactSchema = new HashMap<>();

    public static void createSchema() {

        try {
            if (!object.whatIsNext().equals(ValueType.OBJECT)) {
                throw new InvalidSchemaException("The schema root object must be a JSON object");
            }

            String typeName;
            while ((typeName = object.readObject()) != null) {
                compactSchema.put(typeName, getTypeFromObject(typeName));
            }
            for (String key : compactSchema.keySet()) {
                if (schema.get(key) == null)
                    getType(key);
            }
        } catch (IOException e) {
            throw new JsoundException("Error parsing the JSON file");
        }
    }

    private static TypeDescriptor getType(String key) {
        TypeOrReference typeOrReference = compactSchema.get(key);
        TypeDescriptor typeDescriptor;
        if (typeOrReference.getType() == null)
            typeDescriptor = getType(typeOrReference.getStringType());
        else
            typeDescriptor = typeOrReference.getType();
        schema.put(key, typeDescriptor);
        return typeDescriptor;
    }

    public static TypeOrReference getTypeFromObject(String name) {
        try {
            switch (object.whatIsNext()) {
                case STRING:
                    return parseType(name, object.readString());
                case OBJECT:
                    return new TypeOrReference(buildObjectType(name, new ObjectFacets()));
                case ARRAY:
                    ArrayFacets facets = new ArrayFacets();
                    facets.setContent(name);
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
        Map<String, FieldDescriptor> fieldDescriptors = new LinkedHashMap<>();
        while ((key = object.readObject()) != null) {
            FieldDescriptor fieldDescriptor = new FieldDescriptor();
            boolean allowNull = setMarkers(fieldDescriptor, key);
            if (fieldDescriptors.containsKey(fieldDescriptor.getName()))
                throw new InvalidSchemaException("The field descriptor " + name + " was already defined.");
            setFieldDescriptorType(fieldDescriptor);
            if (allowNull) {
                UnionFacets unionTypeFacets = new UnionFacets();
                if (fieldDescriptor.getType().getStringType() != null)
                    unionTypeFacets.unionContent.getTypes()
                        .add(new TypeOrReference(fieldDescriptor.getType().getStringType()));
                else
                    unionTypeFacets.unionContent.getTypes()
                        .add(new TypeOrReference(fieldDescriptor.getType().getTypeDescriptor()));
                unionTypeFacets.unionContent.getTypes()
                    .add(new TypeOrReference(new NullType("null", new AtomicFacets())));
                fieldDescriptor.setType(new TypeOrReference(new UnionTypeDescriptor(name, unionTypeFacets)));
            }
            fieldDescriptors.put(fieldDescriptor.getName(), fieldDescriptor);
        }
        facets.content = fieldDescriptors;
        return new ObjectTypeDescriptor(name, facets);
    }

    private static void setFieldDescriptorType(FieldDescriptor fieldDescriptor) throws IOException {
        if (object.whatIsNext().equals(ValueType.STRING)) {
            String fieldType = object.readString();
            if (fieldType.contains("=")) {
                fieldType = fieldType.split("=")[0];
                fieldDescriptor.setDefaultValue(ItemFactory.getInstance().createStringItem(fieldType.split("=")[1]));
            }
            if (compactSchema.containsKey(fieldType))
                fieldDescriptor.setType(compactSchema.get(fieldType));
            else
                fieldDescriptor.setType(parseType(fieldDescriptor.name, fieldType));
        } else if (!object.whatIsNext().equals(ValueType.OBJECT))
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
            return new TypeOrReference(new UnionTypeDescriptor(name, facets));
        }
        try {
            return new TypeOrReference(buildAtomicType(AtomicTypes.valueOf(typeString.toUpperCase()), name, false));
        } catch (IllegalArgumentException e) {
            if (compactSchema.containsKey(typeString))
                return compactSchema.get(typeString);
            return new TypeOrReference(typeString);
        }
    }
}
