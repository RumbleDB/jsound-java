package extendedSchemas.facets.minExclusive;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class LessRestrictiveMinExclusiveFacetTest extends BaseTest {
    @Test(expected = LessRestrictiveFacetException.class)
    public void minExclusiveFacetTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/facets/minExclusive/lessRestrictiveMinExclusiveFacet.json",
                "targetType",
                false
        );
    }

}
