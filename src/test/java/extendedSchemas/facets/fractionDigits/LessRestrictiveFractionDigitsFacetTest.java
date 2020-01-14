package extendedSchemas.facets.fractionDigits;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.junit.Test;

import java.io.IOException;

public class LessRestrictiveFractionDigitsFacetTest extends BaseTest {

    @Test(expected = LessRestrictiveFacetException.class)
    public void lengthFacetTest() throws IOException {
        BaseTest.initializeApplication(
                "extendedSchemas/facets/fractionDigits/lessRestrictiveFractionDigitsFacet.json",
                "atomicTypes/decimal/facets/decimalFile.json",
                false
        );
    }

}
