package jsound.atomicTypes;

import jsound.atomicItems.DayTimeDurationItem;
import jsound.atomicItems.DurationItem;
import jsound.facets.AtomicFacets;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.types.ItemTypes;
import org.api.TypeDescriptor;
import org.joda.time.Period;

public class DayTimeDurationType extends DurationType {

    public DayTimeDurationType(String name, AtomicFacets facets) {
        super(ItemTypes.DAYTIMEDURATION, name, facets);
    }

    public DayTimeDurationType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.DAYTIMEDURATION, typeDescriptor);
    }

    @Override
    protected DurationItem createDurationItem(Period period) {
        return new DayTimeDurationItem(period);
    }

    @Override
    public boolean isDayTimeDurationType() {
        return true;
    }

    @Override
    public boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isDayTimeDurationType();
    }
}
