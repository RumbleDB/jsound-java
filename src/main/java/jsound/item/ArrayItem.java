package jsound.item;

import jsound.tyson.TYSONObject;
import org.api.Item;
import org.api.ItemWrapper;

import java.util.List;

public class ArrayItem extends Item {

    private List<ItemWrapper> _items;

    public ArrayItem(List<ItemWrapper> items) {
        super();
        this._items = items;
    }

    @Override
    public List<ItemWrapper> getItems() {
        return _items;
    }

    @Override
    public boolean isArrayItem() {
        return true;
    }

    @Override
    public String getStringAnnotation() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        sb.append('[');
        TYSONObject.newLineIncreaseCounter(sb);
        for (ItemWrapper itemWrapper : _items) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
                TYSONObject.newLine(sb);
            }
            sb.append(itemWrapper.getStringAnnotation());
        }
        TYSONObject.newLineDecreaseCounter(sb);
        sb.append(']');
        return sb.toString();
    }


    public int hashCode() {
        int result = _items.size();
        for (ItemWrapper itemWrapper : _items) {
            result += itemWrapper.getItem().hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ArrayItem))
            return false;
        ArrayItem arrayItem = (ArrayItem) obj;
        if (_items.size() != arrayItem.getItems().size())
            return false;
        for (int i = 0; i < _items.size(); i++) {
            if (!_items.get(i).getItem().equals(arrayItem.getItems().get(i).getItem()))
                return false;
        }
        return true;
    }
}
