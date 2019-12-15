package org.jsound.atomicTypes;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.jsound.atomicItems.YearMonthDurationItem;
import org.jsound.facets.AtomicFacets;
import org.jsound.item.Item;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.ItemTypes;

public class YearMonthDurationType extends DurationType {

    private static final PeriodFormatter _formatter = new PeriodFormatterBuilder().appendLiteral("P")
        .appendYears()
        .appendSuffix("Y")
        .appendMonths()
        .appendSuffix("M")
        .toFormatter();

    public YearMonthDurationType(String name, AtomicFacets facets) {
        super(ItemTypes.YEARMONTHDURATION, name, facets);
    }

    public YearMonthDurationType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.YEARMONTHDURATION, typeDescriptor);
    }

    @Override
    public boolean validate(Item item) {
        Period period;
        try {
            period = getPeriodFromItem(item);
        } catch (IllegalArgumentException e) {
            return false;
        }
        if (this.getFacets() == null)
            return true;
        item = new YearMonthDurationItem(period);
        if (!validateBoundariesFacets(item))
            return false;
        return this.equals(this.baseType.getTypeDescriptor()) || this.baseType.getTypeDescriptor().validate(item);
    }

    @Override
    protected PeriodFormatter getPeriodFormatter() {
        return _formatter;
    }

    @Override
    public boolean isYearMonthDurationType() {
        return true;
    }
}
