package extendedSchemas.atomicTypes.doubleType;

import compactSchemas.atomicTypes.doubleType.DoubleTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedDoubleTest extends DoubleTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        DoubleTest.initializeApplication();
    }
}
