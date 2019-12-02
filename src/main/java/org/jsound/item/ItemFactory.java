package org.jsound.item;

import org.jsound.api.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ItemFactory {

    private static ItemFactory _instance;
    private Item _nullItem;
    private Item _trueBooleanItem;
    private Item _falseBooleanItem;

    public static ItemFactory getInstance() {
        if (_instance == null) {
            _instance = new ItemFactory();
            _instance._nullItem = new NullItem();
            _instance._trueBooleanItem = new BooleanItem(true);
            _instance._falseBooleanItem = new BooleanItem(false);
        }
        return _instance;
    }

    public Item createStringItem(String stringValue) {
        return new StringItem(stringValue);
    }

    public Item createIntegerItem(Integer integerValue) {
        return new IntegerItem(integerValue);
    }

    public Item createDecimalItem(BigDecimal decimalValue) {
        return new DecimalItem(decimalValue);
    }

    public Item createDoubleItem(Double doubleValue) {
        return new DoubleItem(doubleValue);
    }

    public Item createBooleanItem(boolean booleanValue) {
        return booleanValue ? _trueBooleanItem : _falseBooleanItem;
    }

    public Item createNullItem() {
        return _nullItem;
    }

    public Item createObjectItem(Map<String, Item> itemMap) {
        return new ObjectItem(itemMap);
    }

    public Item createArrayItem(List<Item> values) {
        return new ArrayItem(values);
    }
}
