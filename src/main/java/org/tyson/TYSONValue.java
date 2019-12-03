package org.tyson;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.List;
import java.util.Map;

public class TYSONValue {

    TYSONValue() {}

    public static String toTYSONString(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            return "\"" + JSONValue.escape((String)value) + "\"";
        } else if (value instanceof Double) {
            return !((Double)value).isInfinite() && !((Double)value).isNaN() ? value.toString() : "null";
        } else if (value instanceof Float) {
            return !((Float)value).isInfinite() && !((Float)value).isNaN() ? value.toString() : "null";
        } else if (value instanceof Number) {
            return value.toString();
        } else if (value instanceof Boolean) {
            return value.toString();
        } else if (value instanceof JSONAware) {
            return ((JSONAware)value).toJSONString();
        } else if (value instanceof Map) {
            return JSONObject.toJSONString((Map)value);
        } else {
            return value instanceof List ? JSONArray.toJSONString((List)value) : value.toString();
        }
    }
}
