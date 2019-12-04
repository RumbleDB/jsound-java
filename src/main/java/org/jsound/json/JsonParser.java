package org.jsound.json;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import jsound.exceptions.InvalidSchemaException;
import jsound.exceptions.JsoundException;
import jsound.exceptions.UnexpectedTypeException;
import org.jsound.api.Item;
import org.jsound.api.ItemType;
import org.jsound.item.ItemFactory;
import org.jsound.type.ObjectKey;
import org.jsound.type.TypeFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.jsound.api.ItemTypes.*;


public class JsonParser {

    public static Item getItemFromObject(JsonIterator object) {
        try {
            switch (object.whatIsNext()) {
                case STRING:
                    return ItemFactory.getInstance().createStringItem(object.readString());
                case NUMBER:
                    String number = object.readNumberAsString();
                    if (number.contains("E") || number.contains("e")) {
                        return ItemFactory.getInstance().createDoubleItem(Double.parseDouble(number));
                    }
                    if (number.contains(".")) {
                        return ItemFactory.getInstance().createDecimalItem(new BigDecimal(number));
                    }
                    try {
                        return ItemFactory.getInstance().createIntegerItem(Integer.parseInt(number));
                    } catch (NumberFormatException e) {
                        return ItemFactory.getInstance().createDecimalItem(new BigDecimal(number));
                    }
                case BOOLEAN:
                    return ItemFactory.getInstance().createBooleanItem(object.readBoolean());
                case OBJECT:
                    Map<String, Item> itemMap = new HashMap<>();
                    String key;
                    while ((key = object.readObject()) != null) {
                        itemMap.put(key, getItemFromObject(object));
                    }
                    return ItemFactory.getInstance()
                        .createObjectItem(itemMap);
                case ARRAY:
                    List<Item> arrayValues = new ArrayList<>();
                    while (object.readArray()) {
                        try {
                            arrayValues.add(getItemFromObject(object));
                        } catch (ClassCastException e) {
                            throw new UnexpectedTypeException("Array is not containing just JSON objects.");
                        }
                    }
                    return ItemFactory.getInstance().createArrayItem(arrayValues);
                case NULL:
                    object.readNull();
                    return ItemFactory.getInstance().createNullItem();
                default:
                    throw new JsoundException("Invalid value found while parsing. JSON is not well-formed!");
            }
        } catch (IOException e) {
            throw new JsoundException("IO error while parsing. JSON is not well-formed!");
        }
    }

    public static Map<String, ItemType> getRootTypeFromObject(JsonIterator object) {
        try {
            ValueType valueType = object.whatIsNext();
            if (!valueType.equals(ValueType.OBJECT)) {
                throw new InvalidSchemaException("The schema root object must be a JSON object");
            }
            Map<String, ItemType> schema = new HashMap<>();
            String key;
            while ((key = object.readObject()) != null) {
                schema.put(key, getTypeFromObject(object));
            }
            return schema;
        } catch (IOException e) {
            throw new JsoundException("Error parsing the JSON file");
        }
    }

    private static ItemType getTypeFromObject(JsonIterator object) {
        try {
            switch (object.whatIsNext()) {
                case STRING:
                    return parseType(object.readString());
                case OBJECT:
                    Map<ObjectKey, ItemType> typeMap = new LinkedHashMap<>();
                    String key;
                    while ((key = object.readObject()) != null) {
                        typeMap.put(new ObjectKey(key), getTypeFromObject(object));
                    }
                    return TypeFactory.getInstance()
                        .createObjectType(typeMap);
                case ARRAY:
                    ItemType arrayItemsType = null;
                    while (object.readArray()) {
                        arrayItemsType = getTypeFromObject(object);
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
