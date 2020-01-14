package extendedSchemas.facets.atomicTypes.minLength;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

public class LessRestrictiveMinLengthFacetTest extends BaseTest {

    @Test(expected = LessRestrictiveFacetException.class)
    public void lengthFacetTest() throws IOException {
        BaseTest.initializeApplication(
                "extendedSchemas/facets/minLength/lessRestrictiveMinLengthFacet.json",
                "atomicTypes/anyURI/facets/anyURIFile.json",
                false
        );
    }

}
