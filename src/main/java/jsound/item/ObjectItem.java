package jsound.item;

import jsound.tyson.TYSONObject;
import org.api.Item;
import org.api.ItemWrapper;

import java.util.Map;

public class ObjectItem extends Item {
    private Map<String, ItemWrapper> _itemMap;

    ObjectItem(Map<String, ItemWrapper> itemMap) {
        this._itemMap = itemMap;
    }

    @Override
    public Map<String, ItemWrapper> getItemMap() {
        return this._itemMap;
    }

    @Override
    public boolean isObjectItem() {
        return true;
    }

    @Override
    public String getStringAnnotation() {
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        TYSONObject.newLineIncreaseCounter(sb);
        for (String key : _itemMap.keySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
                TYSONObject.newLine(sb);
            }
            sb.append("\"").append(key).append("\"").append(": ").append(_itemMap.get(key).getStringAnnotation());
        }
        TYSONObject.newLineDecreaseCounter(sb);
        sb.append('}');
        return sb.toString();
    }

    public int hashCode() {
        int result = _itemMap.size();
        for (String key : _itemMap.keySet()) {
            result += +_itemMap.get(key).hashCode();
        }
        return result;
    }
}
