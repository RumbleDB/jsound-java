package extendedSchemas.object;

import compactSchemas.object.MissingRequiredFieldTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedMissingRequiredFieldTest extends MissingRequiredFieldTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        MissingRequiredFieldTest.initializeApplication();
    }
}
