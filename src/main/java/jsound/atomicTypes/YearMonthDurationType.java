package jsound.atomicTypes;

import jsound.atomicItems.DurationItem;
import jsound.atomicItems.YearMonthDurationItem;
import jsound.facets.AtomicFacets;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.types.ItemTypes;
import org.api.TypeDescriptor;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

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
    protected DurationItem createDurationItem(Period period) {
        return new YearMonthDurationItem(period);
    }

    @Override
    protected PeriodFormatter getPeriodFormatter() {
        return _formatter;
    }

    @Override
    public boolean isYearMonthDurationType() {
        return true;
    }

    @Override
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isYearMonthDurationType();
    }
}
