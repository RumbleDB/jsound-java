package jsound.atomicTypes;

import org.api.TypeDescriptor;
import jsound.typedescriptors.atomic.AtomicTypeDescriptor;
import jsound.atomicItems.DecimalItem;
import jsound.facets.AtomicFacets;
import jsound.facets.FacetTypes;
import org.api.Item;
import jsound.types.ItemTypes;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static jsound.facets.FacetTypes.FRACTION_DIGITS;
import static jsound.facets.FacetTypes.MAX_EXCLUSIVE;
import static jsound.facets.FacetTypes.MAX_INCLUSIVE;
import static jsound.facets.FacetTypes.MIN_EXCLUSIVE;
import static jsound.facets.FacetTypes.MIN_INCLUSIVE;
import static jsound.facets.FacetTypes.TOTAL_DIGITS;

public class DecimalType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(
            Arrays.asList(
                MIN_INCLUSIVE,
                MAX_INCLUSIVE,
                MIN_EXCLUSIVE,
                MAX_EXCLUSIVE,
                TOTAL_DIGITS,
                FRACTION_DIGITS
            )
    );

    public DecimalType(String name, AtomicFacets facets) {
        super(ItemTypes.DECIMAL, name, facets);
    }

    public DecimalType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.DECIMAL, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(Item item, boolean isEnumValue) {
        BigDecimal decimalValue;
        try {
            if (item.isString())
                decimalValue = new BigDecimal(item.getStringValue());
            else
                decimalValue = item.getDecimalValue();
        } catch (NumberFormatException e) {
            return false;
        }
        if (this.getFacets() == null)
            return true;
        item = new DecimalItem(decimalValue);
        return validateBoundariesFacets(item, isEnumValue) && validateDigitsFacets(item);
    }

    @Override
    protected int compare(Item item1, Item item2) {
        return compareDecimal(item1, item2);
    }

    private int compareDecimal(Item decimalItem, Item constraint) {
        return getDecimalFromItem(decimalItem).compareTo(getDecimalFromItem(constraint));
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) {
        BigDecimal decimalValue = item.getDecimalValue();
        for (Item enumItem : this.getFacets().getEnumeration()) {
            if (decimalValue.compareTo(getDecimalFromItem(enumItem)) == 0)
                return true;
        }
        return false;
    }

    private BigDecimal getDecimalFromItem(Item item) {
        return item.isString()
            ? new BigDecimal(item.getStringValue())
            : item.getDecimalValue();
    }

    @Override
    public void checkAgainstTypeDescriptor(TypeDescriptor typeDescriptor) {
        checkBoundariesAndDigitsFacets(typeDescriptor);
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isDecimalType() {
        return true;
    }

    @Override
    protected boolean hasCompatibleType(TypeDescriptor typeDescriptor) {
        return typeDescriptor.isDecimalType();
    }
}
