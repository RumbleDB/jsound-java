package jsound.atomicItems;

import jsound.item.AtomicItem;
import jsound.types.AtomicTypes;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.ISODateTimeFormat;

import java.util.regex.Pattern;

import static org.joda.time.format.ISODateTimeFormat.dateElementParser;

public class DateTimeItem extends AtomicItem {
    private static final String yearFrag = "((-)?(([1-9]\\d\\d(\\d)+)|(0\\d\\d\\d)))";
    private static final String monthFrag = "((0[1-9])|(1[0-2]))";
    private static final String dayFrag = "((0[1-9])|([1-2]\\d)|(3[0-1]))";
    private static final String hourFrag = "(([0-1]\\d)|(2[0-3]))";
    private static final String minuteFrag = "([0-5]\\d)";
    private static final String secondFrag = "(([0-5]\\d)(\\.(\\d)+)?)";
    private static final String endOfDayFrag = "(24:00:00(\\.(0)+)?)";
    private static final String timezoneFrag = "(Z|([+\\-])(((0\\d|1[0-3]):" + minuteFrag + ")|(14:00)))";
    private static final String dateFrag = "(" + yearFrag + '-' + monthFrag + '-' + dayFrag + ")";
    private static final String timeFrag = "(("
        + hourFrag
        + ":"
        + minuteFrag
        + ":"
        + secondFrag
        + ")|("
        + endOfDayFrag
        + "))";

    private static final String dateTimeLexicalRep = dateFrag + "T" + timeFrag + "(" + timezoneFrag + ")?";
    private static final String dateLexicalRep = "(" + dateFrag + "(" + timezoneFrag + ")?)";
    private static final String timeLexicalRep = "(" + timeFrag + "(" + timezoneFrag + ")?)";

    private static final Pattern dateTimePattern = Pattern.compile(dateTimeLexicalRep);
    private static final Pattern datePattern = Pattern.compile(dateLexicalRep);
    private static final Pattern timePattern = Pattern.compile(timeLexicalRep);


    DateTime _value;
    boolean _hasTimeZone;

    public DateTimeItem(DateTime value, boolean hasTimeZone) {
        this._value = value;
        this._hasTimeZone = hasTimeZone;
    }

    @Override
    public DateTime getDateTime() {
        return _value;
    }

    @Override
    public String getStringValue() {
        String value = this._value.toString();
        String zoneString = this._value.getZone() == DateTimeZone.UTC
            ? "Z"
            : this._value.getZone().toString().equals(DateTimeZone.getDefault().toString())
                ? ""
                : value.substring(value.length() - 6);
        value = value.substring(0, value.length() - zoneString.length());
        value = this._value.getMillisOfSecond() == 0 ? value.substring(0, value.length() - 4) : value;
        return value + (_hasTimeZone ? zoneString : "");
    }

    @Override
    public boolean isDateTimeItem() {
        return true;
    }

    private static boolean checkInvalidDateTimeFormat(String dateTime, AtomicTypes dateTimeType) {
        if (dateTime == null)
            return false;
        switch (dateTimeType) {
            case DATETIME:
                return dateTimePattern.matcher(dateTime).matches();
            case DATE:
                return datePattern.matcher(dateTime).matches();
            case TIME:
                return timePattern.matcher(dateTime).matches();
        }
        return false;
    }

    private static String fixEndOfDay(String dateTime) {
        String endOfDay = "24:00:00";
        String startOfDay = "00:00:00";
        if (dateTime.contains(endOfDay)) {
            if (dateTime.indexOf(endOfDay) == 0)
                return startOfDay;
            int indexOfT = dateTime.indexOf('T');
            if (
                indexOfT < 1
                    || indexOfT != dateTime.indexOf(endOfDay) - 1
                    || !Character.isDigit(dateTime.charAt(indexOfT - 1))
            )
                throw new IllegalArgumentException();
            int dayValue;
            try {
                dayValue = Character.getNumericValue(dateTime.charAt(indexOfT - 1));
            } catch (Exception e) {
                throw new IllegalArgumentException();
            }
            return dateTime.substring(0, indexOfT - 1)
                +
                (dayValue + 1)
                + "T"
                + startOfDay
                +
                dateTime.substring(indexOfT + endOfDay.length() + 1);
        }
        return dateTime;
    }

    public static DateTime parseDateTime(String dateTime, AtomicTypes dateTimeType) throws IllegalArgumentException {
        if (!checkInvalidDateTimeFormat(dateTime, dateTimeType))
            throw new IllegalArgumentException();
        dateTime = fixEndOfDay(dateTime);
        return DateTime.parse(dateTime, getDateTimeFormatter(dateTimeType));
    }

    private static DateTimeFormatter getDateTimeFormatter(AtomicTypes dateTimeType) {
        switch (dateTimeType) {
            case DATETIME:
                return ISODateTimeFormat.dateTimeParser().withOffsetParsed();
            case DATE:
                DateTimeParser dtParser = new DateTimeFormatterBuilder().appendOptional(
                    ((new DateTimeFormatterBuilder()).appendTimeZoneOffset("Z", true, 2, 4).toFormatter()).getParser()
                ).toParser();
                return (new DateTimeFormatterBuilder()).append(dateElementParser())
                    .appendOptional(dtParser)
                    .toFormatter()
                    .withOffsetParsed();
            case TIME:
                return ISODateTimeFormat.timeParser().withOffsetParsed();
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this._value.getMillis());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DateTimeItem && this._value.isEqual(((DateTimeItem) obj).getDateTime());

    }
}
