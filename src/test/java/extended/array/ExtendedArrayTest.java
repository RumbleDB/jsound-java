package extended.array;

import compact.array.ArrayTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedArrayTest extends ArrayTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        ArrayTest.initializeApplication();
    }
}
