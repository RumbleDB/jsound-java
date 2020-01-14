package extendedSchemas.facets.minExclusive;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.junit.Test;

import java.io.IOException;

public class LessRestrictiveMinExclusiveFacetTest extends BaseTest {
    @Test(expected = LessRestrictiveFacetException.class)
    public void lengthFacetTest() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/facets/minExclusive/lessRestrictiveMinExclusiveFacet.json",
                "atomicTypes/dateTime/facets/dateTimeFile.json",
            false
        );
    }

}
