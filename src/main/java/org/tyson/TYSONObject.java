package org.tyson;

import org.json.simple.JSONValue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TYSONObject extends HashMap<String, Object> {

    private String typeName;

    public TYSONObject(String typeName) {
        this.typeName = typeName;
    }

    public String toTYSONString() {
        return toTYSONString(this);
    }

    private String toTYSONString(TYSONObject map) {
        if (map == null) {
            return "null";
        } else {
            StringBuffer sb = new StringBuffer();
            boolean first = true;
            Iterator iter = map.entrySet().iterator();
            sb.append(" (").append(map.typeName).append(") ").append('{');

            while (iter.hasNext()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(',');
                }

                Map.Entry entry = (Map.Entry) iter.next();
                toTYSONString(String.valueOf(entry.getKey()), entry.getValue(), sb);
            }

            sb.append('}');
            return sb.toString();
        }
    }

    private static void toTYSONString(String key, Object value, StringBuffer sb) {
        sb.append('"');
        if (key == null) {
            sb.append("null");
        } else {
            sb.append(JSONValue.escape(key));
        }

        sb.append('"').append(':');
        sb.append(TYSONValue.toTYSONString(value));
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
