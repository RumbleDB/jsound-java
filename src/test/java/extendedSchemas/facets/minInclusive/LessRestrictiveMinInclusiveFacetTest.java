package extendedSchemas.facets.minInclusive;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class LessRestrictiveMinInclusiveFacetTest extends BaseTest {
    @Test(expected = LessRestrictiveFacetException.class)
    public void minInclusiveFacetTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/facets/minInclusive/lessRestrictiveMinInclusiveFacet.json",
                "targetType",
                false
        );
    }

}
