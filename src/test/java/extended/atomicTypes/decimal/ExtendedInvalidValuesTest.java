package extended.atomicTypes.decimal;

import compact.atomicTypes.decimal.InvalidValuesTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedInvalidValuesTest extends InvalidValuesTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        InvalidValuesTest.initializeApplication();
    }
}
