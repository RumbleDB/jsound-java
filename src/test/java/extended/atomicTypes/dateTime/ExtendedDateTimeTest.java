package extended.atomicTypes.dateTime;

import compact.atomicTypes.dateTime.DateTimeTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedDateTimeTest extends DateTimeTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        DateTimeTest.initializeApplication();
    }
}
