package extended.atomicTypes.time;

import compact.atomicTypes.time.TimeTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedTimeTest extends TimeTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        TimeTest.initializeApplication();
    }
}
