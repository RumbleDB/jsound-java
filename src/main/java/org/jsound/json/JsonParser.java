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
import java.util.ArrayList;
import java.util.HashMap;
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
                    return ItemFactory.getInstance().createIntegerItem(object.readInt());
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
                        arrayValues.add(getItemFromObject(object));
                    }
                    return ItemFactory.getInstance().createArrayItem(arrayValues);
                default:
                    return new Item();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Item();
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
                schema.put(key, TypeFactory.getInstance().createUserDefinedItemType(key, getTypeFromObject(object)));
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
                    Map<ObjectKey, ItemType> typeMap = new HashMap<>();
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

    private static ItemType parseType(String typeString) {
        if (typeString.contains(STRING.getTypeName())) {
            return TypeFactory.getInstance().createStringType(typeString);
        }
        else if (typeString.contains(INTEGER.getTypeName())) {
            return TypeFactory.getInstance().createIntegerType(typeString);
        }
        else if (typeString.contains(DECIMAL.getTypeName())) {
            return TypeFactory.getInstance().createDecimalType(typeString);
        }
        else if (typeString.contains(DOUBLE.getTypeName())) {
            return TypeFactory.getInstance().createDoubleType(typeString);
        }
        else if (typeString.contains(BOOLEAN.getTypeName())) {
            return TypeFactory.getInstance().createBooleanType(typeString);
        }
        return TypeFactory.getInstance().createUserDefinedItemType(typeString, new ItemType());
    }
//            if (object.whatIsNext().equals(STRING))
//                return ItemFactory.getInstance().createStringItem(object.readString());
//            if (object.whatIsNext().equals(NUMBER)) {
//                String number = object.readNumberAsString();
//                if (number.contains("E") || number.contains("e")) {
//                    return ItemFactory.getInstance().createDoubleItem(Double.parseDouble(number));
//                }
//                if (number.contains(".") || number.length() >= 12) {
//                    return ItemFactory.getInstance().createDecimalItem(new BigDecimal(number));
//                }
//                try {
//                    return ItemFactory.getInstance().createIntegerItem(Integer.parseInt(number));
//                } catch (NumberFormatException e) {
//                    return ItemFactory.getInstance().createDecimalItem(new BigDecimal(number));
//                }
//            }
//            if (object.whatIsNext().equals(BOOLEAN))
//                return ItemFactory.getInstance().createBooleanItem(object.readBoolean());
//            if (object.whatIsNext().equals(ARRAY)) {
//                List<Item> values = new ArrayList<Item>();
//                while (object.readArray()) {
//                    values.add(getItemFromObject(object, metadata));
//                }
//                return ItemFactory.getInstance().createArrayItem(values);
//            }
//            if (object.whatIsNext().equals(OBJECT)) {
//                List<String> keys = new ArrayList<String>();
//                List<Item> values = new ArrayList<Item>();
//                String s = null;
//                while ((s = object.readObject()) != null) {
//                    keys.add(s);
//                    values.add(getItemFromObject(object, metadata));
//                }
//                return ItemFactory.getInstance()
//                        .createObjectItem(keys, values, ItemMetadata.fromIteratorMetadata(metadata));
//            }
//            if (object.whatIsNext().equals(NULL)) {
//                object.readNull();
//                return ItemFactory.getInstance().createNullItem();
//            }
//            throw new SparksoniqRuntimeException("Invalid value found while parsing. JSON is not well-formed!");
//        } catch (IOException e) {
//            throw new SparksoniqRuntimeException("IO error while parsing. JSON is not well-formed!");
//        }
//    }

}
