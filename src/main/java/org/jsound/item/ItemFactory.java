package org.jsound.item;

import org.jsound.api.Item;

import java.util.List;
import java.util.Map;

public class ItemFactory {

    private static ItemFactory _instance;

    public static ItemFactory getInstance() {
        if (_instance == null) {
            _instance = new ItemFactory();
        }
        return _instance;
    }

    public Item createStringItem(String string) {
        return new StringItem(string);
    }

    public Item createIntegerItem(Integer integer) {
        return new IntegerItem(integer);
    }

    public Item createObjectItem(Map<String, Item> itemMap) {
        return new ObjectItem(itemMap);
    }

    public Item createArrayItem(List<Item> values) {
        return new ArrayItem(values);
    }
}
