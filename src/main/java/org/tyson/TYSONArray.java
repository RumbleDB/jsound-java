package org.tyson;

import java.util.ArrayList;
import java.util.Iterator;

public class TYSONArray extends ArrayList<Object> {

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
            Iterator iter = list.iterator();
            sb.append(" (").append(list.typeName).append(") ").append('[');

            while (iter.hasNext()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(',');
                }

                Object value = iter.next();
                if (value == null) {
                    sb.append("null");
                } else {
                    sb.append(TYSONValue.toTYSONString(value));
                }
            }

            sb.append(']');
            return sb.toString();
        }
    }

    public String toTYSONString() {
        return toTYSONString(this);
    }
}
