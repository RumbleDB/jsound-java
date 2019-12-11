package org.jsound.json;

import com.jsoniter.ValueType;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.JsoundException;
import jsound.exceptions.UnexpectedTypeException;
import org.jsound.atomicTypes.NullType;
import org.jsound.facets.Facets;
import org.jsound.type.ArrayContentDescriptor;
import org.jsound.type.ArrayTypeDescriptor;
import org.jsound.type.AtomicTypes;
import org.jsound.type.FieldDescriptor;
import org.jsound.type.ObjectTypeDescriptor;
import org.jsound.type.TypeDescriptor;
import org.jsound.type.TypeOrReference;
import org.jsound.type.UnionTypeDescriptor;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static org.jsound.cli.JSoundExecutor.object;
import static org.jsound.cli.JSoundExecutor.schema;
import static org.jsound.type.AtomicTypeDescriptor.buildAtomicType;


public class CompactSchemaFileJsonParser {

    private static Set<String> typesToCheck = new HashSet<>();
    private static boolean shouldCheckType = false;
    private static boolean isStringType = false;

    public static void createSchema() {
        try {
            if (!object.whatIsNext().equals(ValueType.OBJECT)) {
                throw new InvalidSchemaException("The schema root object must be a JSON object");
            }

            String key;
            while ((key = object.readObject()) != null) {
                if (object.whatIsNext().equals(ValueType.STRING))
                    isStringType = true;
                TypeDescriptor typeDescriptor = getTypeFromObject(key);
                if (isStringType && shouldCheckType)
                    typesToCheck.add(key);
                schema.put(key, typeDescriptor);
                shouldCheckType = false;
                isStringType = false;
            }
        } catch (IOException e) {
            throw new JsoundException("Error parsing the JSON file");
        }
    }

    private static TypeDescriptor getTypeFromObject(String name) {
        try {
            Facets facets = new Facets();
            switch (object.whatIsNext()) {
                case STRING:
                    return parseType(name, object.readString());
                case OBJECT:
                    return buildObjectType(name, facets);
                case ARRAY:
                    facets.setArrayContentFromObject();
                    if (facets.arrayContent == null)
                        facets.arrayContent = new ArrayContentDescriptor(new TypeOrReference(getTypeFromObject(name)));
                    return new ArrayTypeDescriptor(name, facets);
                default:
                    throw new UnexpectedTypeException("Type for " + name + " is not string nor object nor array.");
            }
        } catch (IOException e) {
            throw new JsoundException("Error parsing the JSON file");
        }
    }

    private static ObjectTypeDescriptor buildObjectType(String name, Facets facets) throws IOException {
        String key;
        Map<String, FieldDescriptor> fieldDescriptors = new LinkedHashMap<>();
        while ((key = object.readObject()) != null) {
            FieldDescriptor fieldDescriptor = new FieldDescriptor();
            boolean allowNull = setMarkers(fieldDescriptor, key);
            if (fieldDescriptors.containsKey(fieldDescriptor.getName()))
                throw new InvalidSchemaException("The field descriptor " + name + " was already defined.");
            setFieldDescriptorType(fieldDescriptor);
            if (allowNull) {
                Facets unionTypeFacets = new Facets();
                if (fieldDescriptor.getType().getStringType() != null)
                    unionTypeFacets.unionContent.getTypes()
                        .add(new TypeOrReference(fieldDescriptor.getType().getStringType()));
                else
                    unionTypeFacets.unionContent.getTypes()
                        .add(new TypeOrReference(fieldDescriptor.getType().getTypeDescriptor()));
                unionTypeFacets.unionContent.getTypes().add(new TypeOrReference(new NullType("null", new Facets())));
                fieldDescriptor.setType(new TypeOrReference(new UnionTypeDescriptor(name, unionTypeFacets)));
            }
            fieldDescriptors.put(fieldDescriptor.getName(), fieldDescriptor);
        }
        facets.objectContent = fieldDescriptors;
        return new ObjectTypeDescriptor(name, facets);
    }

    private static void setFieldDescriptorType(FieldDescriptor fieldDescriptor) throws IOException {
        if (object.whatIsNext().equals(ValueType.STRING)) {
            String fieldType = object.readString();
            if (fieldType.contains("=")) {
                fieldDescriptor.setDefaultValue(fieldType.split("=")[1]);
                fieldType = fieldType.split("=")[0];
            }
            if (schema.containsKey(fieldType))
                fieldDescriptor.setType(new TypeOrReference(schema.get(fieldType)));
            else
                fieldDescriptor.setType(new TypeOrReference(fieldType));
        }
        else if (!object.whatIsNext().equals(ValueType.OBJECT))
            throw new InvalidSchemaException("Type for field descriptors must be either string or object.");
        else
            fieldDescriptor.setType(new TypeOrReference(getTypeFromObject(fieldDescriptor.name)));
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

    private static TypeDescriptor parseType(String name, String typeString) throws IOException {
        if (typeString.contains("|")) {
            Facets facets = new Facets();
            facets.setUnionContent(typeString);
            return new UnionTypeDescriptor(name, facets);
        }
        try {
            return buildAtomicType(AtomicTypes.valueOf(typeString.toUpperCase()), name, false);
        } catch (IllegalArgumentException e) {
            if (schema.containsKey(typeString))
                return schema.get(typeString);
            shouldCheckType = true;
            return null;
        }
    }
}
