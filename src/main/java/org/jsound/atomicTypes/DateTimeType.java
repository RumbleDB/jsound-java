package org.jsound.atomicTypes;

import jsound.exceptions.LessRestrictiveFacetException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.jsound.atomicItems.DateTimeItem;
import org.jsound.facets.AtomicFacets;
import org.jsound.facets.FacetTypes;
import org.jsound.facets.TimezoneFacet;
import org.jsound.item.Item;
import org.jsound.type.AtomicTypeDescriptor;
import org.jsound.type.ItemTypes;
import org.jsound.type.TypeDescriptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.jsound.facets.FacetTypes.EXPLICIT_TIMEZONE;
import static org.jsound.facets.FacetTypes.MAX_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MAX_INCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_INCLUSIVE;

public class DateTimeType extends AtomicTypeDescriptor {

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(
            Arrays.asList(MIN_INCLUSIVE, MAX_INCLUSIVE, MIN_EXCLUSIVE, MAX_EXCLUSIVE, EXPLICIT_TIMEZONE)
    );

    public DateTimeType(String name, AtomicFacets facets) {
        super(ItemTypes.DATETIME, name, facets);
    }

    public DateTimeType(AtomicTypeDescriptor typeDescriptor) {
        super(ItemTypes.DATETIME, typeDescriptor.getName(), typeDescriptor.baseType, typeDescriptor.getFacets());
    }

    @Override
    public boolean validate(Item item, boolean isEnumerationItem) {
        DateTime dateTime;
        try {
            dateTime = getDateTimeFromItem(item);
        } catch (IllegalArgumentException e) {
            return false;
        }
        if (this.getFacets() == null)
            return true;
        item = new DateTimeItem(dateTime);
        if (!validateBoundariesFacets(item, isEnumerationItem))
            return false;
        if (this.getFacets().getDefinedFacets().contains(EXPLICIT_TIMEZONE) && !checkExplicitTimezone(item))
            return false;

        return recursivelyValidate(item);
    }

    private boolean checkExplicitTimezone(Item item) {
        DateTime dateTime = DateTime.parse(
            item.getStringValue(),
            ISODateTimeFormat.dateTimeParser().withOffsetParsed()
        );
        return (item.getStringValue().endsWith("Z")
            || dateTime.getZone() != DateTimeZone.getDefault()
            || !this.getFacets().explicitTimezone.equals(TimezoneFacet.REQUIRED))
            && ((!item.getStringValue().endsWith("Z") && dateTime.getZone() == DateTimeZone.getDefault())
                || !this.getFacets().explicitTimezone.equals(TimezoneFacet.PROHIBITED));
    }

    @Override
    protected boolean validateMinInclusive(Item item) {
        return subtractDateTime(item.getDateTime(), this.getFacets().minInclusive) >= 0;
    }

    @Override
    protected boolean validateMinExclusive(Item item) {
        return subtractDateTime(item.getDateTime(), this.getFacets().minExclusive) > 0;
    }

    @Override
    protected boolean validateMaxInclusive(Item item) {
        return subtractDateTime(item.getDateTime(), this.getFacets().maxInclusive) <= 0;
    }

    @Override
    protected boolean validateMaxExclusive(Item item) {
        return subtractDateTime(item.getDateTime(), this.getFacets().maxExclusive) < 0;
    }

    @Override
    protected boolean validateItemAgainstEnumeration(Item item) {
        DateTime dateTime = item.getDateTime();
        for (Item enumItem : this.getFacets().getEnumeration()) {
            if (dateTime.equals(getDateTimeFromItem(enumItem)))
                return true;
        }
        return false;
    }

    private long subtractDateTime(DateTime itemDateTime, Item constraintItem) {
        return itemDateTime.getMillis() - getDateTimeFromItem(constraintItem).getMillis();
    }

    private DateTime getDateTimeFromItem(Item item) {
        DateTime dateTime = DateTime.parse(
            item.getStringValue(),
            ISODateTimeFormat.dateTimeParser().withOffsetParsed()
        );
        if (!item.getStringValue().endsWith("Z") && dateTime.getZone() == DateTimeZone.getDefault())
            return dateTime.withZoneRetainFields(DateTimeZone.UTC);
        return dateTime;
    }

    @Override
    public void isSubtypeOf(TypeDescriptor typeDescriptor) {
        if (typeDescriptor == null)
            this.subtypeIsValid = true;
        if (this.subtypeIsValid)
            return;
        if (!typeDescriptor.isDateTimeType())
            throw new LessRestrictiveFacetException("Type " + this.getName() + " is not subtype of " + typeDescriptor.getName());
        areBoundariesMoreRestrictive(((AtomicTypeDescriptor) typeDescriptor).getFacets());
        if (this.getFacets().getDefinedFacets().contains(EXPLICIT_TIMEZONE))
            isExplicitTimezoneMoreRestrictive(((AtomicTypeDescriptor) typeDescriptor).getFacets());
        this.subtypeIsValid = true;
        if (this.baseType != null)
            typeDescriptor.isSubtypeOf(typeDescriptor.baseType.getTypeDescriptor());
    }

    @Override
    protected boolean isMinInclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MIN_INCLUSIVE) &&
                subtractDateTime(getDateTimeFromItem(this.getFacets().minInclusive), facets.minInclusive) < 0;
    }

    @Override
    protected boolean isMinExclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MIN_EXCLUSIVE) &&
                subtractDateTime(getDateTimeFromItem(this.getFacets().minExclusive), facets.minExclusive) < 0;
    }

    @Override
    protected boolean isMaxInclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MAX_INCLUSIVE) &&
                subtractDateTime(getDateTimeFromItem(this.getFacets().maxInclusive), facets.maxInclusive) > 0;
    }

    @Override
    protected boolean isMaxExclusiveMoreRestrictive(AtomicFacets facets) {
        return facets.getDefinedFacets().contains(MAX_EXCLUSIVE) &&
                subtractDateTime(getDateTimeFromItem(this.getFacets().maxExclusive), facets.maxExclusive) > 0;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    @Override
    public boolean isDateTimeType() {
        return true;
    }
}
