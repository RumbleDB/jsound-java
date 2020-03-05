package extendedSchemas.atomicTypes.time.facets;

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
    String filePath = "atomicTypes/time/facets/boundariesFacetsError.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/time/facets/timeBoundariesSchema.json",
                "targetType",
                false
        );
    }


    @Test
    public void testInvalidValues() throws IOException {
        TypeDescriptor timeObj = schema.get("timeObj");
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("times").getItem().getItems()) {
            assertFalse(
                timeObj.validate(itemWrapper, false)
            );
        }
    }
}
