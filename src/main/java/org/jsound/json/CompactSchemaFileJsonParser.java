package org.jsound.json;

import com.jsoniter.ValueType;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.JsoundException;
import jsound.exceptions.UnexpectedTypeException;
import org.jsound.api.ArrayContentDescriptor;
import org.jsound.api.ArrayTypeDescriptor;
import org.jsound.api.AtomicTypes;
import org.jsound.api.FieldDescriptor;
import org.jsound.api.ObjectTypeDescriptor;
import org.jsound.api.TypeDescriptor;
import org.jsound.api.UnionTypeDescriptor;
import org.jsound.facets.Facets;
import org.jsound.type.NullType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.jsound.api.AtomicTypeDescriptor.buildAtomicType;
import static org.jsound.cli.JSoundExecutor.object;
import static org.jsound.cli.JSoundExecutor.schema;


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
                        facets.arrayContent = new ArrayContentDescriptor(getTypeFromObject(name));
                    return new ArrayTypeDescriptor(name, facets);
                default:
                    throw new UnexpectedTypeException(object.read().toString());
            }
        } catch (IOException e) {
            throw new JsoundException("Error parsing the JSON file");
        }
    }

    private static ObjectTypeDescriptor buildObjectType(String name, Facets facets) throws IOException {
        String key;
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
        while ((key = object.readObject()) != null) {
            FieldDescriptor fieldDescriptor = new FieldDescriptor();
            boolean allowNull = setMarkers(fieldDescriptor, key);
            setFieldDescriptorType(fieldDescriptor);
            if (allowNull) {
                Facets unionTypeFacets = new Facets();
                if (fieldDescriptor.stringType != null)
                    unionTypeFacets.unionContent.stringTypes.add(fieldDescriptor.stringType);
                else
                    unionTypeFacets.unionContent.types.add(fieldDescriptor.type);
                unionTypeFacets.unionContent.types.add(new NullType("null", new Facets()));
                fieldDescriptor.setType(new UnionTypeDescriptor(name, unionTypeFacets));
            }
            fieldDescriptors.add(fieldDescriptor);
        }
        facets.objectContent = fieldDescriptors;
        return new ObjectTypeDescriptor(name, facets);
    }

    private static void setFieldDescriptorType(FieldDescriptor fieldDescriptor) throws IOException {
        if (object.whatIsNext().equals(ValueType.STRING)) {
            String fieldType = object.readString();
            if (fieldType.contains("=")) {
                fieldDescriptor.setDefaultValue(fieldType.split("=")[1]);
            }
            if (schema.containsKey(fieldType))
                fieldDescriptor.setType(schema.get(fieldType));
            else
                fieldDescriptor.setStringType(fieldType);
        } else if (!object.whatIsNext().equals(ValueType.OBJECT))
            throw new InvalidSchemaException("Type for field descriptors must be either string or object.");
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
