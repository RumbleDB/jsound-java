package extendedSchemas.atomicTypes.hexBinary;

import compactSchemas.atomicTypes.hexBinary.HexBinaryTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedHexBinaryTest extends HexBinaryTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        HexBinaryTest.initializeApplication();
    }
}
