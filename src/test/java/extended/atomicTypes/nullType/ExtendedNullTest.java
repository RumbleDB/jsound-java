package extended.atomicTypes.nullType;

import compact.atomicTypes.nullType.NullTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedNullTest extends NullTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        NullTest.initializeApplication();
    }
}
