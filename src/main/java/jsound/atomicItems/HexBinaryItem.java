package jsound.atomicItems;

import jsound.item.AtomicItem;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;
import java.util.regex.Pattern;

public class HexBinaryItem extends AtomicItem {
    private final static String hexDigit = "[\\da-fA-F]";
    private final static String hexOctet = "(" + hexDigit + hexDigit + ")";
    private final static String hexBinary = hexOctet + "*";
    private final static Pattern hexBinaryPattern = Pattern.compile(hexBinary);

    byte[] _value;
    String _stringValue;

    public HexBinaryItem(byte[] _value, String _stringValue) {
        this._value = _value;
        this._stringValue = _stringValue;
    }

    @Override
    public byte[] getBinaryValue() {
        return _value;
    }

    @Override
    public String getStringValue() {
        return _stringValue;
    }

    @Override
    public boolean isHexBinaryItem() {
        return true;
    }

    private static boolean checkInvalidHexBinaryFormat(String hexBinaryString) {
        return hexBinaryPattern.matcher(hexBinaryString).matches();
    }

    public static byte[] parseHexBinaryString(String hexBinaryString) throws IllegalArgumentException {
        if (hexBinaryString == null || !checkInvalidHexBinaryFormat(hexBinaryString))
            throw new IllegalArgumentException();
        try {
            return Hex.decodeHex(hexBinaryString.toCharArray());
        } catch (DecoderException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HexBinaryItem))
            return false;
        HexBinaryItem hexBinaryItem = (HexBinaryItem) obj;
        return this._stringValue.equals(hexBinaryItem.getStringValue())
            && Arrays.equals(
                this._value,
                hexBinaryItem.getBinaryValue()
            );
    }
}
