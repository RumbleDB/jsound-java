package extendedSchemas.facets.totalDigits;

import base.BaseTest;
import jsound.exceptions.UnexpectedTypeException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

@RunWith(Parameterized.class)
public class InvalidTotalDigitsFacetTest extends BaseTest {

    @Parameterized.Parameter
    public int fileNumber;

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Object[] data() {
        return new Object[] { 1, 2, 3, 4, 5 };
    }

    @Test(expected = UnexpectedTypeException.class)
    public void totalDigitsFacetTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/facets/totalDigits/invalidTotalDigitsFacet" + fileNumber + ".json",
                "targetType",
                false
        );
    }
}
