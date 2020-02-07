package jsound.tyson;

import java.util.ArrayList;
import java.util.Iterator;

public class TYSONArray extends ArrayList<TYSONItem> implements TYSONItem {

    private String typeName;

    public TYSONArray(String typeName) {
        this.typeName = typeName;
    }

    private String toTYSONString(TYSONArray list) {
        if (list == null) {
            return "null";
        } else {
            boolean first = true;
            StringBuilder sb = new StringBuilder();
            Iterator<TYSONItem> iterator = list.iterator();
            if (list.typeName != null)
                sb.append("(\"").append(list.typeName).append("\") ");
            sb.append('[');
            TYSONObject.newLineIncreaseCounter(sb);

            while (iterator.hasNext()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                    TYSONObject.newLine(sb);
                }

                TYSONItem value = iterator.next();
                if (value == null) {
                    sb.append("null");
                } else {
                    sb.append(value.toTYSONString());
                }
            }

            TYSONObject.newLineDecreaseCounter(sb);
            sb.append(']');
            return sb.toString();
        }
    }

    public String toTYSONString() {
        return toTYSONString(this);
    }

    @Override
    public String getTypeName() {
        return typeName;
    }
}
