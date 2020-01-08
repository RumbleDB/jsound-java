package extended.union;

import compact.union.UnionTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class ExtendedUnionTest extends UnionTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        compact = false;
        UnionTest.initializeApplication();
    }
}
