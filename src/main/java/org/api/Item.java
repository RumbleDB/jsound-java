package org.api;

import jsound.exceptions.UnexpectedTypeException;
import org.joda.time.DateTime;
import org.joda.time.Period;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;

public abstract class Item {

    public boolean isAnyURIItem() {
        return false;
    }

    public boolean isStringItem() {
        return false;
    }

    public boolean isBooleanItem() {
        return false;
    }

    public boolean isIntegerItem() {
        return false;
    }

    public boolean isDecimalItem() {
        return false;
    }

    public boolean isDoubleItem() {
        return false;
    }

    public boolean isDateTimeItem() {
        return false;
    }

    public boolean isDateItem() {
        return false;
    }

    public boolean isTimeItem() {
        return false;
    }

    public boolean isDurationItem() {
        return false;
    }

    public boolean isDayTimeDurationItem() {
        return false;
    }

    public boolean isYearMonthDurationItem() {
        return false;
    }

    public boolean isBase64BinaryItem() {
        return false;
    }

    public boolean isHexBinaryItem() {
        return false;
    }

    public boolean isNullItem() {
        return false;
    }

    public boolean isObjectItem() {
        return false;
    }

    public boolean isArrayItem() {
        return false;
    }

    public boolean isAtomicItem() {
        return false;
    }

    public Integer getIntegerValue() {
        throw new UnexpectedTypeException(this.getStringValue() + " does not have an integer.");
    }

    public BigDecimal getDecimalValue() {
        throw new UnexpectedTypeException(this.getStringValue() + " does not have a decimal.");
    }

    public Double getDoubleValue() {
        throw new UnexpectedTypeException(this.getStringValue() + " does not have a double.");
    }

    public String getStringValue() {
        return null;
    }

    public DateTime getDateTime() {
        throw new UnexpectedTypeException(this.getStringValue() + " does not have a dateTime.");
    }

    public URI getAnyURIValue() {
        throw new UnexpectedTypeException(this.getStringValue() + " does not have a URI.");
    }

    public byte[] getBinaryValue() {
        throw new UnexpectedTypeException(this.getStringValue() + " does not have a binary value.");
    }

    public Period getDuration() {
        throw new UnexpectedTypeException(this.getStringValue() + " does not have a period.");
    }

    public Map<String, ItemWrapper> getItemMap() {
        return null;
    }

    public List<ItemWrapper> getItems() {
        return null;
    }

    public String getStringAnnotation() {
        return "\"" + this.getStringValue() + "\"";
    }
}
