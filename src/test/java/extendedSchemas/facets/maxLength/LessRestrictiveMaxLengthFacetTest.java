package extendedSchemas.facets.maxLength;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.junit.Test;

import java.io.IOException;

public class LessRestrictiveMaxLengthFacetTest extends BaseTest {

    @Test(expected = LessRestrictiveFacetException.class)
    public void lengthFacetTest() throws IOException {
        BaseTest.initializeApplication(
                "extendedSchemas/facets/maxLength/lessRestrictiveMaxLengthFacet.json",
                "atomicTypes/anyURI/facets/anyURIFile.json",
                false
        );
    }

}
