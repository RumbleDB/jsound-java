package jsound.atomicItems;

import jsound.item.AtomicItem;

import java.math.BigDecimal;

public class IntegerItem extends AtomicItem {

    private Integer _value;

    public IntegerItem(Integer integer) {
        this._value = integer;
    }

    @Override
    public boolean isIntegerItem() {
        return true;
    }

    @Override
    public Integer getIntegerValue() {
        return _value;
    }

    @Override
    public BigDecimal getDecimalValue() {
        return BigDecimal.valueOf(this._value);
    }

    @Override
    public Double getDoubleValue() {
        return _value.doubleValue();
    }

    @Override
    public String getStringValue() {
        return this._value.toString();
    }

    @Override
    public String getStringAnnotation() {
        return this.getStringValue();
    }

    @Override
    public int hashCode() {
        return this._value.hashCode();
    }
}
