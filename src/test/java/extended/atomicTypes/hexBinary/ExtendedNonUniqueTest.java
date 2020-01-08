package extended.atomicTypes.hexBinary;

import compact.atomicTypes.hexBinary.NonUniqueTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedNonUniqueTest extends NonUniqueTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        NonUniqueTest.initializeApplication();
    }
}
