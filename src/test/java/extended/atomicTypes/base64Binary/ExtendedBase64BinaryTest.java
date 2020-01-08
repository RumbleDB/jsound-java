package extended.atomicTypes.base64Binary;

import compact.atomicTypes.base64Binary.Base64BinaryTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedBase64BinaryTest extends Base64BinaryTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        Base64BinaryTest.initializeApplication();
    }
}
