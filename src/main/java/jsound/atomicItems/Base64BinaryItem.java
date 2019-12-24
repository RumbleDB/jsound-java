package jsound.atomicItems;

import jsound.item.AtomicItem;
import org.apache.commons.codec.binary.Base64;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Base64BinaryItem extends AtomicItem {
    private static final String b04char = "([AQgw])";
    private static final String b04 = "(" + b04char + "(\\s)?)";
    private static final String b16char = "([AEIMQUYcgkosw048])";
    private static final String b16 = "(" + b16char + "(\\s)?)";
    private static final String b64char = "([A-Za-z0-9+/])";
    private static final String b64 = "(" + b64char + "(\\s)?)";
    private static final String padded8 = "(" + b64 + b04 + "=(\\s)?=)";
    private static final String padded16 = "(" + b64 + b64 + b16 + "=)";
    private static final String b64finalQuad = "(" + b64 + b64 + b64 + b64char + ")";
    private static final String b64final = "(" + b64finalQuad + "|" + padded16 + "|" + padded8 + ")";
    private static final String b64quad = "(" + b64 + b64 + b64 + b64 + ")";
    private static final String base64Binary = "((" + b64quad + ")*" + "(" + b64final + "))?";
    private static final Pattern base64BinaryPattern = Pattern.compile(base64Binary);

    byte[] _value;
    String _stringValue;

    public Base64BinaryItem(byte[] _value, String _stringValue) {
        this._value = _value;
        this._stringValue = _stringValue;
    }

    @Override
    public byte[] getBinaryValue() {
        return _value;
    }

    @Override
    public String getStringValue() {
        return Arrays.toString(this._value);
    }

    @Override
    public boolean isBase64BinaryItem() {
        return true;
    }

    private static boolean checkInvalidBase64BinaryFormat(String base64BinaryString) {
        return base64BinaryPattern.matcher(base64BinaryString).matches();
    }

    public static byte[] parseBase64BinaryString(String base64BinaryString) throws IllegalArgumentException {
        if (base64BinaryString == null || !checkInvalidBase64BinaryFormat(base64BinaryString))
            throw new IllegalArgumentException();
        return Base64.decodeBase64(base64BinaryString);
    }
}
