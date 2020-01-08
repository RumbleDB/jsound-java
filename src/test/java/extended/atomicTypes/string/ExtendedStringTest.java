package extended.atomicTypes.string;

import compact.atomicTypes.string.StringTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedStringTest extends StringTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        StringTest.initializeApplication();
    }
}
