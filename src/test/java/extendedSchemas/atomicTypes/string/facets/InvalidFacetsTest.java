package extendedSchemas.atomicTypes.string.facets;

import base.BaseTest;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import org.api.executors.JSoundExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertFalse;

public class InvalidFacetsTest extends BaseTest {
    String filePath = "atomicTypes/string/facets/facetsError.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/string/facets/stringSchema.json",
                "targetType",
                false
        );
    }


    @Test
    public void testInvalidValues() throws IOException {
        TypeDescriptor stringObj = schema.get("stringObj");
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("strings").getItem().getItems()) {
            assertFalse(
                stringObj.validate(itemWrapper, false)
            );
        }
    }
}
