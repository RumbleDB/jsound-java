package extendedSchemas.facets.minInclusive;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.junit.Test;

import java.io.IOException;

public class LessRestrictiveMinInclusiveFacetTest extends BaseTest {
    @Test(expected = LessRestrictiveFacetException.class)
    public void lengthFacetTest() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/facets/minInclusive/lessRestrictiveMinInclusiveFacet.json",
            "atomicTypes/dateTime/facets/dateTimeBoundariesFile.json",
            false
        );
    }

}
