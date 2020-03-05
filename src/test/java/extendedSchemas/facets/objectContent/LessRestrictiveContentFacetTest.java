package extendedSchemas.facets.objectContent;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class LessRestrictiveContentFacetTest extends BaseTest {

    @Test(expected = LessRestrictiveFacetException.class)
    public void contentFacetTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/facets/objectContent/lessRestrictiveContentFacet.json",
                "targetType",
                false
        );
    }

}
