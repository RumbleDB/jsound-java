package org.jsound.facets;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.UnexpectedTypeException;
import org.jsound.api.ArrayContentDescriptor;
import org.jsound.api.FieldDescriptor;
import org.jsound.api.Item;
import org.jsound.api.UnionContentDescriptor;
import org.jsound.json.SchemaFileJsonParser;
import org.jsound.type.Kinds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.jsound.json.InstanceFileJsonParser.getItemFromObject;
import static org.jsound.json.SchemaFileJsonParser.object;
import static org.jsound.json.SchemaFileJsonParser.schema;

public class Facets {
    Integer length = null, minLength = null, maxLength = null;
    String minInclusive = null, maxInclusive = null, minExclusive = null, maxExclusive = null;
    Integer totalDigits = null, fractionDigits = null;
    Item metadata = null;
    List<Item> enumeration = null;
    List<String> constraints = null;
    TimezoneFacet explicitTimezone = null;
    List<FieldDescriptor> objectContent = null;
    ArrayContentDescriptor arrayContent = null;
    UnionContentDescriptor unionContent = null;
    Boolean closed = null;

    public void setFacet(FacetTypes facetType, JsonIterator object, Kinds kind) throws IOException {
        switch (facetType) {
            case LENGTH:
                checkField(this.length, "length");
                this.length = getIntegerFromObject();
                break;
            case MIN_LENGTH:
                checkField(this.minLength, "maxLength");
                this.minLength = getIntegerFromObject();
                break;
            case MAX_LENGTH:
                checkField(this.maxLength, "minLength");
                this.maxLength = getIntegerFromObject();
                break;
            case MIN_INCLUSIVE:
                checkField(this.minInclusive, "minInclusive");
                this.minInclusive = getStringFromObject();
                break;
            case MAX_INCLUSIVE:
                checkField(this.maxInclusive, "maxInclusive");
                this.maxInclusive = getStringFromObject();
                break;
            case MIN_EXCLUSIVE:
                checkField(this.minExclusive, "minExclusive");
                this.minExclusive = getStringFromObject();
                break;
            case MAX_EXCLUSIVE:
                checkField(this.maxExclusive, "maxExclusive");
                this.maxExclusive = getStringFromObject();
                break;
            case TOTAL_DIGITS:
                checkField(this.totalDigits, "totalDigits");
                this.totalDigits = getIntegerFromObject();
                break;
            case FRACTION_DIGITS:
                checkField(this.fractionDigits, "fractionDigits");
                this.fractionDigits = getIntegerFromObject();
                break;
            case EXPLICIT_TIMEZONE:
                checkField(this.explicitTimezone, "explicitTimezone");
                this.explicitTimezone = TimezoneFacet.valueOf(getStringFromObject().toUpperCase());
                break;
            case CONTENT:
                checkField(this.objectContent, "objectContent");
                this.setContentFromObject(kind);
                break;
            case CLOSED:
                checkField(this.closed, "closed");
                this.closed = Boolean.parseBoolean(getStringFromObject());
                break;
            case ENUMERATION:
                checkField(this.enumeration, "enumeration");
                this.enumeration = getEnumerationFromObject();
                break;
            case METADATA:
                checkField(this.metadata, "metadata");
                this.metadata = getItemFromObject(object);
                break;
            case CONSTRAINTS:
                checkField(this.constraints, "maxLength");
                this.constraints = getConstraintsTypeFromObject();
                break;
        }
    }

    private static void checkField(Object key, String fieldName) {
        if (key != null)
            throw new InvalidSchemaException("Field " + fieldName + " is already defined");
    }

    public static String getStringFromObject() throws IOException {
        if (!object.whatIsNext().equals(ValueType.STRING))
            throw new UnexpectedTypeException("Invalid string " + object.read().toString());
        String result = object.readString();
        if (result == null)
            throw new InvalidSchemaException("Invalid null value.");
        return result;
    }

    private static Integer getIntegerFromObject() throws IOException {
        if (!object.whatIsNext().equals(ValueType.NUMBER))
            throw new UnexpectedTypeException("Invalid number " + object.read().toString());
        return object.readInt();
    }

    private static List<Item> getEnumerationFromObject() throws IOException {
        if (!object.whatIsNext().equals(ValueType.ARRAY))
            throw new UnexpectedTypeException("Enumeration should be an array.");
        List<Item> enumerationItemTypes = new ArrayList<>();
        while (object.readArray()) {
            enumerationItemTypes.add(getItemFromObject(object));
        }
        return enumerationItemTypes;
    }

    private static List<String> getConstraintsTypeFromObject() throws IOException {
        if (!object.whatIsNext().equals(ValueType.ARRAY))
            throw new UnexpectedTypeException("Constraints should be an array.");
        List<String> constraints = new ArrayList<>();
        while (object.readArray()) {
            constraints.add(getStringFromObject());
        }
        return constraints;
    }

    private void setContentFromObject(Kinds kind) throws IOException {
        if (!object.whatIsNext().equals(ValueType.ARRAY))
            throw new UnexpectedTypeException("Content property should be an array.");
        switch (kind) {
            case ATOMIC:
                throw new InvalidSchemaException("Cannot have content facet for atomic type.");
            case OBJECT:
                this.setObjectContentFromObject();
                break;
            case ARRAY:
                this.setArrayContentFromObject();
                break;
            case UNION:
                this.setUnionContentFromObject();
                break;
        }
    }

    private void setObjectContentFromObject() throws IOException {
        String key;
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
        Set<String> fieldsNames = new HashSet<>();
        while (object.readArray()) {
            FieldDescriptor fieldDescriptor = new FieldDescriptor();
            while ((key = object.readObject()) != null) {
                switch (key) {
                    case "name":
                        String name = getStringFromObject();
                        if (fieldsNames.contains(name))
                            throw new InvalidSchemaException("The field descriptor " + name + " was already defined.");
                        fieldDescriptor.setName(name);
                        fieldsNames.add(name);
                        break;
                    case "type":
                        setFieldDescriptorType(fieldDescriptor, object);
                        break;
                    case "required":
                        fieldDescriptor.setRequired(Boolean.parseBoolean(getStringFromObject()));
                        break;
                    case "unique":
                        fieldDescriptor.setUnique(Boolean.parseBoolean(getStringFromObject()));
                        break;
                    case "default":
                        fieldDescriptor.setDefaultValue(getItemFromObject(object));
                        break;
                    default:
                        throw new InvalidSchemaException(key + " is not a valid property for the field descriptor.");
                }
            }
            fieldDescriptors.add(fieldDescriptor);
        }
        this.objectContent = fieldDescriptors;
    }

    private void setArrayContentFromObject() throws IOException {
        int size = 0;
        while (object.readArray()) {
            if (size > 0)
                throw new InvalidSchemaException("Can only specify one type for the array content type.");
            if (object.whatIsNext().equals(ValueType.STRING)) {
                String contentType = object.readString();
                if (schema.containsKey(contentType))
                    this.arrayContent = new ArrayContentDescriptor(schema.get(contentType));
                else
                    this.arrayContent = new ArrayContentDescriptor(object.readString());
            } else
                this.arrayContent = new ArrayContentDescriptor(SchemaFileJsonParser.getTypeDescriptor());
            size++;
        }
        if (size == 0)
            throw new InvalidSchemaException("You must specify the content type for array.");
    }

    private void setUnionContentFromObject() throws IOException {
        UnionContentDescriptor unionContent = new UnionContentDescriptor();
        while (object.readArray()) {
            if (object.whatIsNext().equals(ValueType.STRING)) {
                String type = object.readString();
                if (schema.containsKey(type))
                    unionContent.types.add(schema.get(type));
                else
                    unionContent.stringTypes.add(type);
            } else
                unionContent.types.add(SchemaFileJsonParser.getTypeDescriptor());
        }
        this.unionContent = unionContent;
    }

    private static void setFieldDescriptorType(FieldDescriptor fieldDescriptor, JsonIterator object)
            throws IOException {
        if (object.whatIsNext().equals(ValueType.STRING)) {
            String fieldType = object.readString();
            if (schema.containsKey(fieldType))
                fieldDescriptor.setType(schema.get(fieldType));
            else
                fieldDescriptor.setStringType(fieldType);
        } else if (!object.whatIsNext().equals(ValueType.OBJECT))
            throw new InvalidSchemaException("Type for field descriptors must be either string or object.");
        fieldDescriptor.setType(SchemaFileJsonParser.getTypeDescriptor());
    }
}
