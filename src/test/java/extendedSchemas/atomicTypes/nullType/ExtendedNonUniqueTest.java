package extendedSchemas.atomicTypes.nullType;

import compactSchemas.atomicTypes.nullType.NonUniqueTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedNonUniqueTest extends NonUniqueTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        NonUniqueTest.initializeApplication();
    }
}
