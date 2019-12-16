package org.jsound.item;

import jsound.exceptions.UnexpectedTypeException;
import org.joda.time.DateTime;
import org.joda.time.Period;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Map;

public abstract class Item {

    public boolean isString() {
        return false;
    }

    public boolean isInteger() {
        return false;
    }

    public boolean isDecimal() {
        return false;
    }

    public boolean isDouble() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isYearMonthDurationItem() {
        return false;
    }

    public boolean isNull() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isArray() {
        return false;
    }

    public Integer getIntegerValue() {
        throw new UnexpectedTypeException(this.getStringValue() + " does not have an integer.");
    }

    public BigDecimal castToDecimalValue() {
        throw new UnexpectedTypeException(this.getStringValue() + " cannot be cast to decimal.");
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

    public Map<String, Item> getItemMap() {
        return null;
    }

    public String getStringAnnotation() {
        return "\"" + this.getStringValue() + "\"";
    }
}
