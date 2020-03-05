package extendedSchemas.facets.totalDigits;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class LessRestrictiveTotalDigitsFacetTest extends BaseTest {

    @Test(expected = LessRestrictiveFacetException.class)
    public void totalDigitsFacetTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/facets/totalDigits/lessRestrictiveTotalDigitsFacet.json",
                "targetType",
                false
        );
    }

}
