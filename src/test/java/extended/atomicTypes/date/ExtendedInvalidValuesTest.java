package extended.atomicTypes.date;

import compact.atomicTypes.date.InvalidValuesTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedInvalidValuesTest extends InvalidValuesTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        InvalidValuesTest.initializeApplication();
    }
}
