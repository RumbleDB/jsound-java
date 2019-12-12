package org.jsound.facets;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.UnexpectedTypeException;
import org.jsound.item.Item;
import org.jsound.json.SchemaFileJsonParser;
import org.jsound.kinds.Kinds;
import org.jsound.type.ArrayContentDescriptor;
import org.jsound.type.FieldDescriptor;
import org.jsound.type.TypeOrReference;
import org.jsound.type.UnionContentDescriptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.jsound.cli.JSoundExecutor.object;
import static org.jsound.cli.JSoundExecutor.schema;
import static org.jsound.json.CompactSchemaFileJsonParser.compactSchema;
import static org.jsound.json.CompactSchemaFileJsonParser.getTypeFromObject;
import static org.jsound.json.InstanceFileJsonParser.getItemFromObject;

public class Facets {
    Integer length = null, minLength = null, maxLength = null;
    String minInclusive = null, maxInclusive = null, minExclusive = null, maxExclusive = null;
    Integer totalDigits = null, fractionDigits = null;
    Item metadata = null;
    private List<Item> enumeration = null;
    List<String> constraints = null;
    TimezoneFacet explicitTimezone = null;
    public Map<String, FieldDescriptor> objectContent = null;
    public ArrayContentDescriptor arrayContent = null;
    public UnionContentDescriptor unionContent = null;
    private Boolean closed = null;
    private Boolean hasEnumeration, hasDefaultValue = false;

    private Set<FacetTypes> definedFacets = new HashSet<>();

    public void setFacet(FacetTypes facetType, JsonIterator object, Kinds kind) throws IOException {
        definedFacets.add(facetType);
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
                this.hasEnumeration = true;
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

    public Boolean hasEnumeration() {
        return hasEnumeration;
    }

    public Boolean hasDefaultValue() {
        return hasDefaultValue;
    }

    public Set<FacetTypes> getDefinedFacets() {
        return definedFacets;
    }

    public List<Item> getEnumeration() {
        return enumeration;
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
                throw new InvalidSchemaException("Cannot have content facet for atomic atomicTypes.");
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
        this.objectContent = fieldDescriptors;
    }

    public void setArrayContentFromObject() throws IOException {
        int size = 0;
        while (object.readArray()) {
            if (size > 0)
                throw new InvalidSchemaException("Can only specify one atomicTypes for the array content atomicTypes.");
            if (object.whatIsNext().equals(ValueType.STRING)) {
                String contentType = object.readString();
                if (schema.containsKey(contentType))
                    this.arrayContent = new ArrayContentDescriptor(new TypeOrReference(schema.get(contentType)));
                else
                    this.arrayContent = new ArrayContentDescriptor(new TypeOrReference(contentType));
            }
            size++;
        }
        if (size == 0)
            throw new InvalidSchemaException("You must specify the content atomicTypes for array.");
        if (this.arrayContent == null)
            this.arrayContent = new ArrayContentDescriptor(
                    new TypeOrReference(SchemaFileJsonParser.getTypeDescriptor())
            );
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
                unionContent.getTypes().add(new TypeOrReference(SchemaFileJsonParser.getTypeDescriptor()));
        }
        this.unionContent = unionContent;
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

    public void setArrayContent(String name) throws IOException {
        int size = 0;
        while (object.readArray()) {
            if (size > 0)
                throw new InvalidSchemaException("Can only specify one atomicTypes for the array content atomicTypes.");
            if (object.whatIsNext().equals(ValueType.STRING)) {
                String contentType = object.readString();
                if (schema.containsKey(contentType))
                    this.arrayContent = new ArrayContentDescriptor(compactSchema.get(contentType));
                else
                    this.arrayContent = new ArrayContentDescriptor(new TypeOrReference(contentType));
            }
            size++;
        }
        if (size == 0)
            throw new InvalidSchemaException("You must specify the content atomicTypes for array.");
        if (arrayContent == null)
            arrayContent = new ArrayContentDescriptor(getTypeFromObject(name));
    }
}
