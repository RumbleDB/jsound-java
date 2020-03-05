package extendedSchemas.facets.arrayContent;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class LessRestrictiveContentFacetTest extends BaseTest {

    @Test(expected = LessRestrictiveFacetException.class)
    public void contentFacetTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/facets/arrayContent/lessRestrictiveContentFacet.json",
                "targetType",
                false
        );
    }

}
