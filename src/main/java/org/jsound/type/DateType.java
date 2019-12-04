package org.jsound.type;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.jsound.api.AtomicType;
import org.jsound.api.ItemTypes;

import static org.joda.time.format.ISODateTimeFormat.dateElementParser;

public class DateType extends AtomicType {

    private String _defaultValue;
    private static final DateTimeParser dtParser = new DateTimeFormatterBuilder().appendOptional(
        ((new DateTimeFormatterBuilder()).appendTimeZoneOffset("Z", true, 2, 4).toFormatter()).getParser()
    ).toParser();
    private static final DateTimeFormatter _formatter = new DateTimeFormatterBuilder().append(dateElementParser())
        .appendOptional(dtParser)
        .toFormatter()
        .withOffsetParsed();

    DateType(String typeString) {
        super(ItemTypes.DATE, typeString);
    }

    @Override
    protected void setDefaultValue(String typeString) {
        _defaultValue = typeString.contains("=") ? typeString.split("=")[1] : null;
    }

    public String getDefaultValue() {
        return this._defaultValue;
    }

    @Override
    public String getDefaultValueStringAnnotation() {
        return "\"" + this._defaultValue + "\"";
    }

    @Override
    public boolean isDateType() {
        return true;
    }

    public static DateTimeFormatter getFormatter() {
        return _formatter;
    }
}
