package extended.atomicTypes.date;

import compact.atomicTypes.date.DateTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedDateTest extends DateTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        DateTest.initializeApplication();
    }
}
