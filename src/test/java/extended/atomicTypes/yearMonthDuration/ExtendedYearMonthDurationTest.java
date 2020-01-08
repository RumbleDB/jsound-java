package extended.atomicTypes.yearMonthDuration;

import compact.atomicTypes.yearMonthDuration.YearMonthDurationTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedYearMonthDurationTest extends YearMonthDurationTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        YearMonthDurationTest.initializeApplication();
    }
}
