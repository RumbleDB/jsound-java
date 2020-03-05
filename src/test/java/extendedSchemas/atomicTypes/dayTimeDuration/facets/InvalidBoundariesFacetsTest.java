package extendedSchemas.atomicTypes.dayTimeDuration.facets;

import base.BaseTest;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import org.api.executors.JSoundExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertFalse;

public class InvalidBoundariesFacetsTest extends BaseTest {
    String filePath = "atomicTypes/dayTimeDuration/facets/boundariesFacetsError.json";
    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/dayTimeDuration/facets/dayTimeDurationBoundariesSchema.json",
                "targetType",
                false
        );
    }


    @Test
    public void testInvalidValues() throws IOException {
        TypeDescriptor dayTimeDurationObj = schema.get("dayTimeDurationObj");
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("dayTimeDurations").getItem().getItems()) {
            assertFalse(
                dayTimeDurationObj.validate(itemWrapper, false)
            );
        }
    }
}
