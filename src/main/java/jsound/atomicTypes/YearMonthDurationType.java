package jsound.atomicTypes;

import jsound.atomicItems.DurationItem;
import jsound.atomicItems.YearMonthDurationItem;
import jsound.facets.AtomicFacets;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.types.ItemTypes;
import org.api.TypeDescriptor;
import org.joda.time.Period;

public class YearMonthDurationType extends DurationType {

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
    public boolean isYearMonthDurationType() {
        return true;
    }

    @Override
    public boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isYearMonthDurationType();
    }
}
