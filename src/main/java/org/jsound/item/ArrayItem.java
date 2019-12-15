package org.jsound.item;

import java.util.List;

public class ArrayItem extends Item {

    private List<Item> _items;

    ArrayItem(List<Item> items) {
        super();
        this._items = items;
    }

    public List<Item> getItems() {
        return _items;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public String getStringAnnotation() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        sb.append('[');
        for (Item item : _items) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(item.getStringAnnotation());
        }
        sb.append(']');
        return sb.toString();
    }


    public int hashCode() {
        int result = _items.size();
        for (Item item : _items) {
            result += item.hashCode();
        }
        return result;
    }
}
