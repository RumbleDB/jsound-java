package jsound.json;

import com.jsoniter.JsonIterator;
import jsound.exceptions.JsoundException;
import jsound.exceptions.UnexpectedTypeException;
import org.api.Item;
import jsound.item.ItemFactory;
import org.api.ItemWrapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InstanceFileJsonParser {

    public static ItemWrapper getItemFromObject(JsonIterator object) {
        try {
            switch (object.whatIsNext()) {
                case STRING:
                    return new ItemWrapper(ItemFactory.getInstance().createStringItem(object.readString()));
                case NUMBER:
                    String number = object.readNumberAsString();
                    if (number.contains("E") || number.contains("e")) {
                        return new ItemWrapper(ItemFactory.getInstance().createDoubleItem(Double.parseDouble(number)));
                    }
                    if (number.contains(".")) {
                        return new ItemWrapper(ItemFactory.getInstance().createDecimalItem(new BigDecimal(number)));
                    }
                    try {
                        return new ItemWrapper(ItemFactory.getInstance().createIntegerItem(Integer.parseInt(number)));
                    } catch (NumberFormatException e) {
                        return new ItemWrapper(ItemFactory.getInstance().createDecimalItem(new BigDecimal(number)));
                    }
                case BOOLEAN:
                    return new ItemWrapper(ItemFactory.getInstance().createBooleanItem(object.readBoolean()));
                case OBJECT:
                    Map<String, ItemWrapper> itemMap = new HashMap<>();
                    String key;
                    while ((key = object.readObject()) != null) {
                        itemMap.put(key, getItemFromObject(object));
                    }
                    return new ItemWrapper(
                            ItemFactory.getInstance()
                                .createObjectItem(itemMap)
                    );
                case ARRAY:
                    List<ItemWrapper> arrayValues = new ArrayList<>();
                    while (object.readArray()) {
                        try {
                            arrayValues.add(getItemFromObject(object));
                        } catch (ClassCastException e) {
                            throw new UnexpectedTypeException("Array is not containing just JSON objects.");
                        }
                    }
                    return new ItemWrapper(ItemFactory.getInstance().createArrayItem(arrayValues));
                case NULL:
                    object.readNull();
                    return new ItemWrapper(ItemFactory.getInstance().createNullItem());
                default:
                    throw new JsoundException("Invalid value found while parsing. JSON is not well-formed!");
            }
        } catch (IOException e) {
            throw new JsoundException("IO error while parsing. JSON is not well-formed!");
        }
    }
}
