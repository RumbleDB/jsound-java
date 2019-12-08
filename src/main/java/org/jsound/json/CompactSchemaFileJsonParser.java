package org.jsound.json;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.JsoundException;
import jsound.exceptions.UnexpectedTypeException;
import org.jsound.api.ItemType;
import org.jsound.type.ObjectKey;
import org.jsound.type.TypeFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.jsound.api.ItemTypes.*;
import static org.jsound.api.ItemTypes.NULL;

public class CompactSchemaFileJsonParser {

    public static JsonIterator object;

    public static Map<String, ItemType> getRootTypes() {
        try {
            if (!object.whatIsNext().equals(ValueType.OBJECT)) {
                throw new InvalidSchemaException("The schema root object must be a JSON object");
            }
            Map<String, ItemType> schema = new HashMap<>();
            String key;
            while ((key = object.readObject()) != null) {
                schema.put(key, getTypeFromObject());
            }
            return schema;
        } catch (IOException e) {
            throw new JsoundException("Error parsing the JSON file");
        }
    }

    private static ItemType getTypeFromObject() {
        try {
            switch (object.whatIsNext()) {
                case STRING:
                    return parseType(object.readString());
                case OBJECT:
                    Map<ObjectKey, ItemType> typeMap = new LinkedHashMap<>();
                    String key;
                    while ((key = object.readObject()) != null) {
                        typeMap.put(new ObjectKey(key, true), getTypeFromObject());
                    }
                    return TypeFactory.getInstance()
                        .createObjectType(typeMap);
                case ARRAY:
                    ItemType arrayItemsType = null;
                    while (object.readArray()) {
                        arrayItemsType = getTypeFromObject();
                    }
                    return TypeFactory.getInstance().createArrayType(arrayItemsType);
                default:
                    throw new UnexpectedTypeException(object.read().toString());
            }
        } catch (IOException e) {
            throw new JsoundException("Error parsing the JSON file");
        }
    }

    public static ItemType parseType(String typeString) {
        if (typeString.contains("|")) {
            return TypeFactory.getInstance().createUnionType(typeString);
        } else if (typeString.contains(STRING.getTypeName())) {
            return TypeFactory.getInstance().createStringType(typeString);
        } else if (typeString.contains(INTEGER.getTypeName())) {
            return TypeFactory.getInstance().createIntegerType(typeString);
        } else if (typeString.contains(DECIMAL.getTypeName())) {
            return TypeFactory.getInstance().createDecimalType(typeString);
        } else if (typeString.contains(DOUBLE.getTypeName())) {
            return TypeFactory.getInstance().createDoubleType(typeString);
        } else if (typeString.contains(BOOLEAN.getTypeName())) {
            return TypeFactory.getInstance().createBooleanType(typeString);
        } else if (typeString.contains(DATETIME.getTypeName())) {
            return TypeFactory.getInstance().createDateTimeType(typeString);
        } else if (typeString.contains(DATE.getTypeName())) {
            return TypeFactory.getInstance().createDateType(typeString);
        } else if (typeString.contains(TIME.getTypeName())) {
            return TypeFactory.getInstance().createTimeType(typeString);
        } else if (typeString.contains(DURATION.getTypeName())) {
            return TypeFactory.getInstance().createDurationType(typeString);
        } else if (typeString.contains(YEARMONTHDURATION.getTypeName())) {
            return TypeFactory.getInstance().createYearMonthDurationType(typeString);
        } else if (typeString.contains(DAYTIMEDURATION.getTypeName())) {
            return TypeFactory.getInstance().createDayTimeDurationType(typeString);
        } else if (typeString.contains(HEXBINARY.getTypeName())) {
            return TypeFactory.getInstance().createHexBinaryType(typeString);
        } else if (typeString.contains(BASE64BINARY.getTypeName())) {
            return TypeFactory.getInstance().createBase64BinaryType(typeString);
        } else if (typeString.contains(NULL.getTypeName())) {
            return TypeFactory.getInstance().createNullType();
        }
        return TypeFactory.getInstance().createUserDefinedItemType(typeString, null);
    }
}
