package extendedSchemas.facets.length;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

@RunWith(Parameterized.class)
public class LessRestrictiveLengthFacetTest extends BaseTest {

    @Parameterized.Parameter
    public int fileNumber;

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Object[] data() {
        return new Object[] { 1, 2 };
    }

    @Test(expected = LessRestrictiveFacetException.class)
    public void lengthFacetTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/facets/length/lessRestrictiveLengthFacet" + fileNumber + ".json",
                "targetType",
                false
        );
    }

}
