package extended.atomicTypes.duration;

import compact.atomicTypes.duration.DurationTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedDurationTest extends DurationTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        DurationTest.initializeApplication();
    }
}
