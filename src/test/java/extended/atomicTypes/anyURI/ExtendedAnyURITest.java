package extended.atomicTypes.anyURI;

import compact.atomicTypes.anyURI.AnyURITest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedAnyURITest extends AnyURITest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        AnyURITest.initializeApplication();
    }
}
