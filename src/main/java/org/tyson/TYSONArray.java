package org.tyson;

import java.util.ArrayList;
import java.util.Iterator;

public class TYSONArray extends ArrayList<TysonItem> implements TysonItem {

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
            Iterator<TysonItem> iterator = list.iterator();
            sb.append("(\"").append(list.typeName).append("\") ").append('[');
            TYSONObject.newLineIncreaseCounter(sb);

            while (iterator.hasNext()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                    TYSONObject.newLine(sb);
                }

                TysonItem value = iterator.next();
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
}
