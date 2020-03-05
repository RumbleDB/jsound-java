package extendedSchemas.facets.maxExclusive;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class LessRestrictiveMaxExclusiveFacetTest extends BaseTest {
    @Test(expected = LessRestrictiveFacetException.class)
    public void maxExclusiveFacetTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/facets/maxExclusive/lessRestrictiveMaxExclusiveFacet.json",
                "targetType",
                false
        );
    }

}
