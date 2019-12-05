package org.jsound.json;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.JsoundException;
import jsound.exceptions.UnexpectedTypeException;
import org.jsound.api.Item;
import org.jsound.api.ItemType;
import org.jsound.type.Kinds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchemaFileJsonParser {
    private static Map<String, ItemType> schema = new HashMap<>();

    public static Map<String, ItemType> getRootType(JsonIterator object) {
        try {
            if (!object.whatIsNext().equals(ValueType.OBJECT)) {
                throw new InvalidSchemaException("The schema root object must be a JSON object");
            }
            String types = object.readObject();
            if (!("types".equals(types))) {
                throw new InvalidSchemaException("There should be a root array of types called \"types\"");
            }
            if (!object.whatIsNext().equals(ValueType.ARRAY)) {
                throw new InvalidSchemaException("Please provide an array of types");
            }
            while (object.readArray()) {
                getRootTypes(object);
            }
            return schema;
        } catch (IOException e) {
            throw new JsoundException("Error parsing the JSON file");
        }
    }

    private static void getRootTypes(JsonIterator object) throws IOException {
        if (object.whatIsNext() != ValueType.OBJECT)
            throw new UnexpectedTypeException(object.read().toString());
        parseTypeDescriptor(object);
    }

    private static void parseTypeDescriptor(JsonIterator object) throws IOException {
        String key;
        String name = null;
        Kinds kind = null;
        String baseType = null;
        Item metadata = null;
        List<Item> enumeration = null;
        List<String> constraints = null;
        while ((key = object.readObject()) != null) {
            switch (key) {
                case "name":
                    name = getStringFromObject(object);
                    break;
                case "kind":
                    kind = getKindFromObject(object);
                    break;
                case "baseType":
                    baseType = getStringFromObject(object);
                    break;
                case "metadata":
                    metadata = InstanceFileJsonParser.getItemFromObject(object);
                    break;
                case "enumeration":
                    enumeration = getEnumerationFromObject(object);
                    break;
                case "constraints":
                    constraints = getConstraintsTypeFromObject(object);
                    break;
            }
        }
        if (name == null || kind == null)
            throw new InvalidSchemaException("Missing name or kind");
        if (baseType == null)
            baseType = setImplicitBaseType(kind);
        ItemType type = buildType(object, kind, baseType, metadata, enumeration, constraints);
        if (enumeration != null) {
            for (Item item : enumeration) {
                if (!item.isValidAgainst(type))
                    throw new InvalidSchemaException(
                            item.getStringAnnotation() + " in the enumeration is not valid against the type at hand."
                    );
            }
        }
        schema.put(name, type);
    }

    private static ItemType buildType(
            JsonIterator object,
            Kinds kind,
            String baseType,
            Item metadata,
            List<Item> enumeration,
            List<String> constraints
    ) {
        return null;
    }

    private static String getStringFromObject(JsonIterator object) throws IOException {
        if (!object.whatIsNext().equals(ValueType.STRING))
            throw new UnexpectedTypeException("Invalid string " + object.read().toString());
        return object.readString();
    }

    private static Kinds getKindFromObject(JsonIterator object) throws IOException {
        if (!object.whatIsNext().equals(ValueType.STRING))
            throw new UnexpectedTypeException("Invalid kind " + object.read().toString());
        String kind = object.readString();
        try {
            return Kinds.valueOf(kind.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnexpectedTypeException("Invalid kind " + object.read().toString());
        }
    }

    private static List<Item> getEnumerationFromObject(JsonIterator object) throws IOException {
        if (!object.whatIsNext().equals(ValueType.ARRAY))
            throw new UnexpectedTypeException("Enumeration should be an array.");
        List<Item> enumerationItemTypes = new ArrayList<>();
        while (object.readArray()) {
            enumerationItemTypes.add(InstanceFileJsonParser.getItemFromObject(object));
        }
        return enumerationItemTypes;
    }

    private static List<String> getConstraintsTypeFromObject(JsonIterator object) throws IOException {
        if (!object.whatIsNext().equals(ValueType.ARRAY))
            throw new UnexpectedTypeException("Constraints should be an array.");
        List<String> constraints = new ArrayList<>();
        while (object.readArray()) {
            constraints.add(getStringFromObject(object));
        }
        return constraints;
    }

    private static String setImplicitBaseType(Kinds kind) {
        switch (kind) {
            case ATOMIC:
                throw new InvalidSchemaException("BaseType must be defined for kind atomic");
            case OBJECT:
            case ARRAY:
                return kind.getTypeName();
            case UNION:
                return "value";
        }
        return null;
    }

    // private static ItemType getTypeFromObject(JsonIterator object) {
    // try {
    // switch (object.whatIsNext()) {
    // case STRING:
    // return object.readString();
    // case OBJECT:
    // Map<ObjectKey, ItemType> typeMap = new LinkedHashMap<>();
    // String key;
    // while ((key = object.readObject()) != null) {
    // typeMap.put(new ObjectKey(key, true), getTypeFromObject(object));
    // }
    // return TypeFactory.getInstance()
    // .createObjectType(typeMap);
    // case ARRAY:
    // ItemType arrayItemsType = null;
    // while (object.readArray()) {
    // arrayItemsType = getTypeFromObject(object);
    // }
    // return TypeFactory.getInstance().createArrayType(arrayItemsType);
    // default:
    // throw new UnexpectedTypeException(object.read().toString());
    // }
    // } catch (IOException e) {
    // throw new JsoundException("Error parsing the JSON file");
    // }
    // }
    //
    // public static ItemType parseBaseType(String typeString) {
    // if (typeString.contains("|")) {
    // return TypeFactory.getInstance().createUnionType(typeString);
    // } else if (typeString.contains(STRING.getTypeName())) {
    // return TypeFactory.getInstance().createStringType(typeString);
    // } else if (typeString.contains(INTEGER.getTypeName())) {
    // return TypeFactory.getInstance().createIntegerType(typeString);
    // } else if (typeString.contains(DECIMAL.getTypeName())) {
    // return TypeFactory.getInstance().createDecimalType(typeString);
    // } else if (typeString.contains(DOUBLE.getTypeName())) {
    // return TypeFactory.getInstance().createDoubleType(typeString);
    // } else if (typeString.contains(BOOLEAN.getTypeName())) {
    // return TypeFactory.getInstance().createBooleanType(typeString);
    // } else if (typeString.contains(DATETIME.getTypeName())) {
    // return TypeFactory.getInstance().createDateTimeType(typeString);
    // } else if (typeString.contains(DATE.getTypeName())) {
    // return TypeFactory.getInstance().createDateType(typeString);
    // } else if (typeString.contains(TIME.getTypeName())) {
    // return TypeFactory.getInstance().createTimeType(typeString);
    // } else if (typeString.contains(DURATION.getTypeName())) {
    // return TypeFactory.getInstance().createDurationType(typeString);
    // } else if (typeString.contains(YEARMONTHDURATION.getTypeName())) {
    // return TypeFactory.getInstance().createYearMonthDurationType(typeString);
    // } else if (typeString.contains(DAYTIMEDURATION.getTypeName())) {
    // return TypeFactory.getInstance().createDayTimeDurationType(typeString);
    // } else if (typeString.contains(HEXBINARY.getTypeName())) {
    // return TypeFactory.getInstance().createHexBinaryType(typeString);
    // } else if (typeString.contains(BASE64BINARY.getTypeName())) {
    // return TypeFactory.getInstance().createBase64BinaryType(typeString);
    // } else if (typeString.contains(NULL.getTypeName())) {
    // return TypeFactory.getInstance().createNullType();
    // }
    // return TypeFactory.getInstance().createUserDefinedItemType(typeString, null);
    // }
}
