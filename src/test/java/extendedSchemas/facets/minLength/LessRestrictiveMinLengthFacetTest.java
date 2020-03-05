package extendedSchemas.facets.minLength;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class LessRestrictiveMinLengthFacetTest extends BaseTest {

    @Test(expected = LessRestrictiveFacetException.class)
    public void minLengthFacetTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/facets/minLength/lessRestrictiveMinLengthFacet.json",
                "targetType",
                false
        );
    }

}
