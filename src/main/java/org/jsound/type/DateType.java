package org.jsound.type;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.jsound.api.AtomicTypeDescriptor;
import org.jsound.api.ItemTypes;
import org.jsound.facets.FacetTypes;
import org.jsound.facets.Facets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.joda.time.format.ISODateTimeFormat.dateElementParser;
import static org.jsound.facets.FacetTypes.EXPLICIT_TIMEZONE;
import static org.jsound.facets.FacetTypes.MAX_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MAX_INCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_EXCLUSIVE;
import static org.jsound.facets.FacetTypes.MIN_INCLUSIVE;

public class DateType extends AtomicTypeDescriptor {

    private static final DateTimeParser dtParser = new DateTimeFormatterBuilder().appendOptional(
        ((new DateTimeFormatterBuilder()).appendTimeZoneOffset("Z", true, 2, 4).toFormatter()).getParser()
    ).toParser();
    private static final DateTimeFormatter _formatter = new DateTimeFormatterBuilder().append(dateElementParser())
        .appendOptional(dtParser)
        .toFormatter()
        .withOffsetParsed();

    public static final Set<FacetTypes> _allowedFacets = new HashSet<>(
            Arrays.asList(MIN_INCLUSIVE, MAX_INCLUSIVE, MIN_EXCLUSIVE, MAX_EXCLUSIVE, EXPLICIT_TIMEZONE)
    );

    public DateType(String name, Facets facets) {
        super(ItemTypes.DATE, name, facets);
        this.baseType = this;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return _allowedFacets;
    }

    public static DateTimeFormatter getFormatter() {
        return _formatter;
    }
}
