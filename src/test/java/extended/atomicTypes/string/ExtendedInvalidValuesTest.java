package extended.atomicTypes.string;

import compact.atomicTypes.string.InvalidValuesTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedInvalidValuesTest extends InvalidValuesTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        InvalidValuesTest.initializeApplication();
    }
}
