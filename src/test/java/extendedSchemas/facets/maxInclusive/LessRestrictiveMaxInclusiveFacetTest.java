package extendedSchemas.facets.maxInclusive;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.junit.Test;

import java.io.IOException;

public class LessRestrictiveMaxInclusiveFacetTest extends BaseTest {
    @Test(expected = LessRestrictiveFacetException.class)
    public void lengthFacetTest() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/facets/maxInclusive/lessRestrictiveMaxInclusiveFacet.json",
                "atomicTypes/dateTime/facets/dateTimeFile.json",
            false
        );
    }

}
