package org.jsound.item;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.ISOPeriodFormat;
import org.jsound.api.AtomicItem;
import org.jsound.api.TypeDescriptor;
import org.jsound.type.DateType;
import org.jsound.type.DayTimeDurationType;
import org.jsound.type.DurationType;
import org.jsound.type.YearMonthDurationType;
import org.jsound.utils.StringUtils;


public class StringItem extends AtomicItem {

    private String _value;

    StringItem(String string) {
        this._value = string;
    }

    @Override
    public String getValue() {
        return this._value;
    }

    @Override
    public String getStringAnnotation() {
        return "\"" + this._value + "\"";
    }

    @Override
    public boolean isValidAgainst(TypeDescriptor typeDescriptor) {
        if (typeDescriptor.isStringType())
            return true;
        try {
            if (typeDescriptor.isIntegerType()) {
                Integer.parseInt(this._value);
            } else if (typeDescriptor.isDecimalType()) {
                if (this._value.contains("e") || this._value.contains("E"))
                    return false;
                Float.parseFloat(this._value);
            } else if (typeDescriptor.isDoubleType()) {
                Double.parseDouble(this._value);
            } else if (typeDescriptor.isBooleanType()) {
                return StringUtils.isBooleanLiteral(this._value);
            } else if (typeDescriptor.isDateTimeType()) {
                DateTime.parse(this._value, ISODateTimeFormat.dateTimeParser().withOffsetParsed());
            } else if (typeDescriptor.isDateType()) {
                DateTime.parse(this._value, DateType.getFormatter());
            } else if (typeDescriptor.isTimeType()) {
                DateTime.parse(this._value, ISODateTimeFormat.timeParser().withOffsetParsed());
            } else if (typeDescriptor.isYearMonthDurationType()) {
                Period.parse(DurationType.getPositivePeriod(this._value), YearMonthDurationType.getFormatter());
            } else if (typeDescriptor.isDayTimeDurationType()) {
                Period.parse(DurationType.getPositivePeriod(this._value), DayTimeDurationType.getFormatter());
            } else if (typeDescriptor.isDurationType()) {
                Period.parse(DurationType.getPositivePeriod(this._value), ISOPeriodFormat.standard());
            } else if (typeDescriptor.isHexBinaryType()) {
                Hex.decodeHex(this._value.toCharArray());
            } else if (typeDescriptor.isBase64BinaryType()) {
                Base64.decodeBase64(this._value);
            } else if (typeDescriptor.isNullType()) {
                return StringUtils.isNullLiteral(this._value);
            } else if (typeDescriptor.isUnionType()) {
                return super.isValidAgainst(typeDescriptor);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this._value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StringItem))
            return false;
        return this._value.equals(((StringItem) o)._value);
    }
}
