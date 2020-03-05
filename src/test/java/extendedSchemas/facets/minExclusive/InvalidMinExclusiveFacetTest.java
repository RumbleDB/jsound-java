package extendedSchemas.facets.minExclusive;

import base.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

import org.api.executors.JSoundExecutor;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class InvalidMinExclusiveFacetTest extends BaseTest {
    String filePath = "atomicTypes/dateTime/facets/dateTimeBoundariesFile.json";

    @Parameterized.Parameter
    public int fileNumber;

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Object[] data() {
        return new Object[] { 1, 2, 3, 4, 5 };
    }

    @Test(expected = IllegalArgumentException.class)
    public void minExclusiveFacetTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/facets/minExclusive/invalidMinExclusiveFacet" + fileNumber + ".json",
                "targetType",
                false
        );

        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
