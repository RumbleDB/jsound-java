package extended.object;

import compact.object.ObjectTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedObjectTest extends ObjectTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        ObjectTest.initializeApplication();
    }
}
