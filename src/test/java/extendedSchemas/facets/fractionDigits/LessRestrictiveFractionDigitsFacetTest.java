package extendedSchemas.facets.fractionDigits;

import base.BaseTest;
import jsound.exceptions.LessRestrictiveFacetException;
import org.api.executors.JSoundExecutor;
import org.junit.Test;

import java.io.IOException;

public class LessRestrictiveFractionDigitsFacetTest extends BaseTest {

    @Test(expected = LessRestrictiveFacetException.class)
    public void fractionDigitFacetTest() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/facets/fractionDigits/lessRestrictiveFractionDigitsFacet.json",
                "targetType",
                false
        );
    }

}
