package extendedSchemas.facets.arrayContent;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

public class LessRestrictiveContentFacetTest extends BaseTest {

    @Test(expected = LessRestrictiveFacetException.class)
    public void contentFacetTest() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/facets/arrayContent/lessRestrictiveContentFacet.json",
            "array/arrayFile.json",
            false
        );
    }

}
