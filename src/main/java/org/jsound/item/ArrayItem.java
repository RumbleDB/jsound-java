package org.jsound.item;

import org.tyson.TYSONObject;

import java.util.List;

public class ArrayItem extends Item {

    private List<Item> _items;

    ArrayItem(List<Item> items) {
        super();
        this._items = items;
    }

    @Override
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
        TYSONObject.newLineIncreaseCounter(sb);
        for (Item item : _items) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
                TYSONObject.newLine(sb);
            }
            sb.append(item.getStringAnnotation());
        }
        TYSONObject.newLineDecreaseCounter(sb);
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
