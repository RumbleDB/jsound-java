package jsound.atomicItems;

import jsound.item.AtomicItem;

import java.math.BigDecimal;

public class DecimalItem extends AtomicItem {

    private BigDecimal _value;

    public DecimalItem(BigDecimal value) {
        this._value = value;
    }

    @Override
    public BigDecimal getDecimalValue() {
        return _value;
    }

    @Override
    public BigDecimal castToDecimalValue() {
        return _value;
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
