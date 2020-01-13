package jsound.atomicItems;

import jsound.item.AtomicItem;
import org.api.Item;

import java.math.BigDecimal;

public class DoubleItem extends AtomicItem {

    private Double _value;

    public DoubleItem(Double value) {
        this._value = value;
    }

    @Override
    public boolean isDoubleItem() {
        return true;
    }

    @Override
    public Integer getIntegerValue() {
        return this._value.intValue();
    }

    @Override
    public BigDecimal getDecimalValue() {
        if (Double.isNaN(this.getDoubleValue()) || Double.isInfinite(this.getDoubleValue()))
            return super.getDecimalValue();
        return BigDecimal.valueOf(getDoubleValue());
    }

    @Override
    public Double getDoubleValue() {
        return _value;
    }

    @Override
    public String getStringValue() {
        if (Double.isNaN(this.getDoubleValue()) || Double.isInfinite(this.getDoubleValue()))
            return String.valueOf(this.getDoubleValue());
        boolean negativeZero = this.getDoubleValue() == 0 && String.valueOf(this.getDoubleValue()).charAt(0) == ('-');
        String doubleString = String.valueOf(this.getDecimalValue().stripTrailingZeros().toPlainString());
        return negativeZero ? '-' + doubleString : doubleString;
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
        return (obj instanceof IntegerItem || obj instanceof DecimalItem || obj instanceof DoubleItem)
            && this._value.compareTo(((Item) obj).getDoubleValue()) == 0;
    }
}
