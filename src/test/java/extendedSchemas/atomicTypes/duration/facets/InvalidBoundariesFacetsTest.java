package extendedSchemas.atomicTypes.duration.facets;

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
    String filePath = "atomicTypes/duration/facets/boundariesFacetsError.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/duration/facets/durationBoundariesSchema.json",
                "targetType",
                false
        );
    }


    @Test
    public void testInvalidValues() throws IOException {
        TypeDescriptor durationObj = schema.get("durationObj");
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("durations").getItem().getItems()) {
            if (
                durationObj.validate(itemWrapper, false)
            )
                System.out.println("hello");
        }
    }
}
