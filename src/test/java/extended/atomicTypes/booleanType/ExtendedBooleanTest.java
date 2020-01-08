package extended.atomicTypes.booleanType;

import compact.atomicTypes.booleanType.BooleanTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedBooleanTest extends BooleanTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        BooleanTest.initializeApplication();
    }
}
