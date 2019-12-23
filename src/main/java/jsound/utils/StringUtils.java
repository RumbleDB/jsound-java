package jsound.utils;

public class StringUtils {
    public static boolean isBooleanLiteral(String value) {
        return "true".equals(value) || "false".equals(value);
    }

    public static boolean isNullLiteral(String value) {
        return "null".equals(value);
    }
}
