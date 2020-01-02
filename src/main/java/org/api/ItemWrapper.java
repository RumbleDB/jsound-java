package org.api;

import jsound.exceptions.UnexpectedTypeException;
import org.joda.time.DateTime;
import org.joda.time.Period;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class ItemWrapper {
    private Item item;

    public ItemWrapper(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public boolean isAnyURIItem() {
        return this.item.isAnyURIItem();
    }

    public boolean isStringItem() {
        return this.item.isStringItem();
    }

    public boolean isBooleanItem() {
        return this.item.isBooleanItem();
    }

    public boolean isIntegerItem() {
        return item.isIntegerItem();
    }

    public boolean isDecimalItem() {
        return item.isDecimalItem();
    }

    public boolean isDoubleItem() {
        return item.isDoubleItem();
    }

    public boolean isDateTimeItem() {
        return this.item.isDateTimeItem();
    }

    public boolean isDateItem() {
        return this.item.isDateItem();
    }

    public boolean isTimeItem() {
        return this.item.isTimeItem();
    }

    public boolean isDurationItem() {
        return this.item.isDurationItem();
    }

    public boolean isDayTimeDurationItem() {
        return item.isDayTimeDurationItem();
    }

    public boolean isBase64BinaryItem() {
        return this.item.isBase64BinaryItem();
    }

    public boolean isHexBinaryItem() {
        return this.item.isHexBinaryItem();
    }

    public boolean isNullItem() {
        return this.item.isNullItem();
    }

    public boolean isObjectItem() {
        return this.item.isObjectItem();
    }

    public boolean isArrayItem() {
        return this.item.isArrayItem();
    }


    public Integer getIntegerValue() {
        return this.item.getIntegerValue();
    }

    public BigDecimal getDecimalValue() {
        return this.item.getDecimalValue();
    }

    public Double getDoubleValue() {
        return this.item.getDoubleValue();
    }

    public String getStringValue() {
        return this.item.getStringValue();
    }

    public DateTime getDateTime() {
        return this.item.getDateTime();
    }

    public URI getAnyURIValue() {
        return this.item.getAnyURIValue();
    }

    public byte[] getBinaryValue() {
        return this.item.getBinaryValue();
    }

    public Period getDuration() {
        return this.item.getDuration();
    }

    public Map<String, ItemWrapper> getItemMap() {
        return this.item.getItemMap();
    }

    public List<ItemWrapper> getItems() {
        return this.item.getItems();
    }

    public String getStringAnnotation() {
        return this.item.getStringAnnotation();
    }

    public boolean isYearMonthDurationItem() {
        return item.isYearMonthDurationItem();
    }
}
