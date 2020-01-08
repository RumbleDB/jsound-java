package extended;

import compact.GeneralTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedGeneralTest extends GeneralTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        GeneralTest.initializeApplication();
    }
}
