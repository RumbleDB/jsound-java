package jsound.atomicItems;

import jsound.item.AtomicItem;
import jsound.types.AtomicTypes;
import jsound.types.ItemTypes;
import org.api.Item;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.regex.Pattern;


public class DurationItem extends AtomicItem {
    private static final String prefix = "(-)?P";
    private static final String duYearFrag = "(\\d)+Y";
    private static final String duMonthFrag = "(\\d)+M";
    private static final String duDayFrag = "(\\d)+D";
    private static final String duHourFrag = "(\\d)+H";
    private static final String duMinuteFrag = "(\\d)+M";
    private static final String duSecondFrag = "(((\\d)+)|(\\.(\\d)+)|((\\d)+\\.(\\d)+))S";

    private static final String duYearMonthFrag = "((" + duYearFrag + "(" + duMonthFrag + ")?)|" + duMonthFrag + ")";
    private static final String duTimeFrag = "T(("
            + duHourFrag
            + "("
            + duMinuteFrag
            + ")?"
            + "("
            + duSecondFrag
            + ")?)|"
            +
            "("
            + duMinuteFrag
            + "("
            + duSecondFrag
            + ")?)|"
            + duSecondFrag
            + ")";
    private static final String duDayTimeFrag = "((" + duDayFrag + "(" + duTimeFrag + ")?)|" + duTimeFrag + ")";
    private static final String durationLiteral = prefix
            + "(("
            + duYearMonthFrag
            + "("
            + duDayTimeFrag
            + ")?)|"
            + duDayTimeFrag
            + ")";
    private static final String yearMonthDurationLiteral = prefix + duYearMonthFrag;
    private static final String dayTimeDurationLiteral = prefix + duDayTimeFrag;
    private static final Pattern durationPattern = Pattern.compile(durationLiteral);
    private static final Pattern yearMonthDurationPattern = Pattern.compile(yearMonthDurationLiteral);
    private static final Pattern dayTimeDurationPattern = Pattern.compile(dayTimeDurationLiteral);

    Period _value;
    boolean isNegative;

    public DurationItem(Period value) {
        this._value = value;
        isNegative = this._value.toString().contains("-");
    }

    @Override
    public Period getDuration() {
        return _value;
    }

    @Override
    public String getStringValue() {
        if (this.isNegative) {
            return '-' + this._value.negated().toString();
        }
        return this._value.toString();
    }

    @Override
    public boolean isDurationItem() {
        return true;
    }

    private static PeriodFormatter getPeriodFormatter(ItemTypes durationType) {
        switch (durationType) {
            case DURATION:
                return ISOPeriodFormat.standard();
            case YEARMONTHDURATION:
                return new PeriodFormatterBuilder().appendLiteral("P")
                        .appendYears()
                        .appendSuffix("Y")
                        .appendMonths()
                        .appendSuffix("M")
                        .toFormatter();
            case DAYTIMEDURATION:
                return new PeriodFormatterBuilder().appendLiteral("P")
                        .appendDays()
                        .appendSuffix("D")
                        .appendSeparatorIfFieldsAfter("T")
                        .appendHours()
                        .appendSuffix("H")
                        .appendMinutes()
                        .appendSuffix("M")
                        .appendSecondsWithOptionalMillis()
                        .appendSuffix("S")
                        .toFormatter();
            default:
                throw new IllegalArgumentException();
        }
    }

    private static PeriodType getPeriodType(ItemTypes durationType) {
        switch (durationType) {
            case DURATION:
                return PeriodType.yearMonthDayTime();
            case YEARMONTHDURATION:
                return PeriodType.forFields(
                        new DurationFieldType[] { DurationFieldType.years(), DurationFieldType.months() }
                );
            case DAYTIMEDURATION:
                return PeriodType.dayTime();
            default:
                throw new IllegalArgumentException();
        }
    }

    private static boolean checkInvalidDurationFormat(String duration, ItemTypes durationType) {
        switch (durationType) {
            case DURATION:
                return durationPattern.matcher(duration).matches();
            case YEARMONTHDURATION:
                return yearMonthDurationPattern.matcher(duration).matches();
            case DAYTIMEDURATION:
                return dayTimeDurationPattern.matcher(duration).matches();
        }
        return false;
    }

    public static Period getDurationFromString(String duration, ItemTypes durationType)
            throws UnsupportedOperationException,
            IllegalArgumentException {
        if (durationType == null || !checkInvalidDurationFormat(duration, durationType))
            throw new IllegalArgumentException();
        boolean isNegative = duration.charAt(0) == '-';
        if (isNegative)
            duration = duration.substring(1);
        PeriodFormatter pf = getPeriodFormatter(durationType);
        Period period = Period.parse(duration, pf);
        return isNegative
                ? period.negated().normalizedStandard(getPeriodType(durationType))
                : period.normalizedStandard(getPeriodType(durationType));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Item)) {
            return false;
        }
        Item item = (Item) obj;
        Instant now = new Instant();
        if (item.isDurationItem()) {
            return this.getDuration()
                .toDurationFrom(now)
                .isEqual(item.getDuration().toDurationFrom(now));
        }
        return false;
    }

}
