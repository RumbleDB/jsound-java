package extended.atomicTypes.dayTimeDuration;

import compact.atomicTypes.dayTimeDuration.DayTimeDurationTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedDayTimeDurationTest extends DayTimeDurationTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        DayTimeDurationTest.initializeApplication();
    }
}
