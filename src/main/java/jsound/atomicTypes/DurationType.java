package jsound.atomicTypes;

import jsound.atomicItems.DurationItem;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.types.ItemTypes;
import org.api.Item;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import org.joda.time.Instant;
import org.joda.time.Period;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static jsound.atomicItems.DurationItem.getDurationFromString;
import static jsound.facets.FacetTypes.MAXEXCLUSIVE;
import static jsound.facets.FacetTypes.MAXINCLUSIVE;
import static jsound.facets.FacetTypes.MINEXCLUSIVE;
import static jsound.facets.FacetTypes.MININCLUSIVE;

public class DurationType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(
            Arrays.asList(MININCLUSIVE, MAXINCLUSIVE, MINEXCLUSIVE, MAXEXCLUSIVE)
    );

    public DurationType(String name, AtomicFacets facets) {
        super(ItemTypes.DURATION, name, facets);
    }

    public DurationType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.DURATION, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    DurationType(ItemTypes durationSubtype, String name, AtomicFacets facets) {
        super(durationSubtype, name, facets);
    }

    DurationType(ItemTypes durationSubtype, AtomicTypeDescriptor typeDescriptor) {
        super(durationSubtype, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isDurationType() {
        return true;
    }

    @Override
    public boolean validate(ItemWrapper itemWrapper, boolean isEnumValue) {
        Period period;
        try {
            period = getDurationFromItem(itemWrapper.getItem());
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            return false;
        }
        itemWrapper.setItem(createDurationItem(period));
        return this.getFacets() == null || validateBoundariesFacets(itemWrapper.getItem(), isEnumValue);
    }

    protected DurationItem createDurationItem(Period period) {
        return new DurationItem(period);
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) {
        Period period = item.getDuration();
        for (ItemWrapper enumItem : this.getFacets().getEnumeration()) {
            if (period.equals(getDurationFromItem(enumItem.getItem())))
                return true;
        }
        return false;
    }

    @Override
    protected int compare(Item item1, Item item2) {
        return compareDuration(item1, item2);
    }

    private int compareDuration(Item durationItem, Item constraintItem) {
        return Long.compare(
            getDurationFromItem(durationItem).minus(getDurationFromItem(constraintItem))
                .toDurationFrom(Instant.now())
                .getMillis(),
            0L
        );
    }

    protected Period getDurationFromItem(Item item) {
        if (item.isDurationItem())
            return item.getDuration();
        return getDurationFromString(item.getStringValue(), this.getType());
    }

    @Override
    public void checkAgainstTypeDescriptor(TypeDescriptor typeDescriptor) {
        checkBoundariesFacet(typeDescriptor);
    }

    @Override
    public boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isDurationType();
    }
}
