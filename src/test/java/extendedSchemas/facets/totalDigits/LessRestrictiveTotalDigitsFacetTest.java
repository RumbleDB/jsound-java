package extendedSchemas.facets.totalDigits;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.junit.Test;

import java.io.IOException;

public class LessRestrictiveTotalDigitsFacetTest extends BaseTest {

    @Test(expected = LessRestrictiveFacetException.class)
    public void lengthFacetTest() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/facets/totalDigits/lessRestrictiveTotalDigitsFacet.json",
            "atomicTypes/decimal/facets/decimalFile.json",
            false
        );
    }

}
