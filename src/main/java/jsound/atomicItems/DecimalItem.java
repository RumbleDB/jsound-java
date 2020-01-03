package jsound.atomicItems;

import jsound.item.AtomicItem;
import org.api.Item;

import java.math.BigDecimal;

public class DecimalItem extends AtomicItem {

    private BigDecimal _value;

    public DecimalItem(BigDecimal value) {
        this._value = value;
    }

    @Override
    public boolean isDecimalItem() {
        return true;
    }

    @Override
    public Integer getIntegerValue() {
        return this._value.intValue();
    }

    @Override
    public BigDecimal getDecimalValue() {
        return _value;
    }

    @Override
    public Double getDoubleValue() {
        return this._value.doubleValue();
    }

    @Override
    public String getStringValue() {
        return String.valueOf(_value.stripTrailingZeros().toPlainString());
    }

    @Override
    public String getStringAnnotation() {
        return this.getStringValue();
    }

    @Override
    public int hashCode() {
        return this._value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Item) && this._value.compareTo(((Item) obj).getDecimalValue()) == 0;
    }
}
