package extended.atomicTypes.decimal;

import compact.atomicTypes.decimal.DecimalTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedDecimalTest extends DecimalTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        DecimalTest.initializeApplication();
    }
}
