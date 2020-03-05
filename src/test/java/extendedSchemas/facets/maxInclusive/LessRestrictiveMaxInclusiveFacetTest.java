package extendedSchemas.facets.maxInclusive;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class LessRestrictiveMaxInclusiveFacetTest extends BaseTest {
    @Test(expected = LessRestrictiveFacetException.class)
    public void maxInclusiveFacetTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/facets/maxInclusive/lessRestrictiveMaxInclusiveFacet.json",
                "targetType",
                false
        );
    }

}
