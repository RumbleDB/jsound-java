package extendedSchemas.atomicTypes.integer;

import compactSchemas.atomicTypes.integer.IntegerTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedIntegerTest extends IntegerTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        IntegerTest.initializeApplication();
    }
}
