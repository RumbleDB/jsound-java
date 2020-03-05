package extendedSchemas.facets.maxLength;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class LessRestrictiveMaxLengthFacetTest extends BaseTest {

    @Test(expected = LessRestrictiveFacetException.class)
    public void maxLengthFacetTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/facets/maxLength/lessRestrictiveMaxLengthFacet.json",
                "targetType",
                false
        );
    }

}
