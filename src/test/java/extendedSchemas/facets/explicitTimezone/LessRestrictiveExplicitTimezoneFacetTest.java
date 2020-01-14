package extendedSchemas.facets.explicitTimezone;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

@RunWith(Parameterized.class)
public class LessRestrictiveExplicitTimezoneFacetTest extends BaseTest {

    @Parameterized.Parameter
    public int fileNumber;

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Object[] data() {
        return new Object[] { 1, 2 };
    }

    @Test(expected = LessRestrictiveFacetException.class)
    public void lengthFacetTest() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/facets/explicitTimezone/lessRestrictiveExplicitTimezoneFacet" + fileNumber + ".json",
            "atomicTypes/dateTime/facets/dateTimeTimezoneFile.json",
            false
        );
    }

}
