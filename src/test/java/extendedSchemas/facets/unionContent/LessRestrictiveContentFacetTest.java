package extendedSchemas.facets.unionContent;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.junit.Test;

import java.io.IOException;

public class LessRestrictiveContentFacetTest extends BaseTest {

    @Test(expected = LessRestrictiveFacetException.class)
    public void contentFacetTest() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/facets/unionContent/lessRestrictiveContentFacet.json",
            "union/unionFile.json",
            false
        );
    }

}
