package jsound.tyson;

import org.apache.commons.text.StringEscapeUtils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class TYSONObject extends LinkedHashMap<String, TysonItem> implements TysonItem {

    private String typeName;
    public static int tabCounter = 0;

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
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            Iterator<Map.Entry<String, TysonItem>> iterator = map.entrySet().iterator();
            sb.append("(\"").append(map.typeName).append("\") ").append('{');
            newLineIncreaseCounter(sb);

            while (iterator.hasNext()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                    newLine(sb);
                }

                Map.Entry<String, TysonItem> entry = iterator.next();
                toTYSONString(String.valueOf(entry.getKey()), entry.getValue(), sb);
            }

            newLineDecreaseCounter(sb);
            sb.append('}');
            return sb.toString();
        }
    }

    private static void toTYSONString(String key, TysonItem value, StringBuilder sb) {
        sb.append('"');
        if (key == null) {
            sb.append("null");
        } else {
            sb.append(StringEscapeUtils.escapeJava(key));
        }

        sb.append('"').append(": ");
        sb.append(value.toTYSONString());
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public static void newLineIncreaseCounter(StringBuilder sb) {
        sb.append("\n");
        tabCounter++;
        format(sb);
    }

    public static void newLine(StringBuilder sb) {
        sb.append("\n");
        format(sb);
    }

    private static void format(StringBuilder sb) {
        sb.append("    ".repeat(Math.max(0, tabCounter)));
    }

    public static void newLineDecreaseCounter(StringBuilder sb) {
        sb.append("\n");
        tabCounter--;
        format(sb);
    }

    @Override
    public String getTypeName() {
        return typeName;
    }
}
