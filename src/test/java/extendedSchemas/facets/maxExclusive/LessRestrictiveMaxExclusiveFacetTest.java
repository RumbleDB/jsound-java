package extendedSchemas.facets.maxExclusive;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.junit.Test;

import java.io.IOException;

public class LessRestrictiveMaxExclusiveFacetTest extends BaseTest {
    @Test(expected = LessRestrictiveFacetException.class)
    public void lengthFacetTest() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/facets/maxExclusive/lessRestrictiveMaxExclusiveFacet.json",
                "atomicTypes/dateTime/facets/dateTimeFile.json",
            false
        );
    }

}
