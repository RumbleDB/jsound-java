package extendedSchemas.atomicTypes.base64Binary.facets;

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
    String filePath = "atomicTypes/base64Binary/facets/facetsError.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/base64Binary/facets/base64BinarySchema.json",
                "targetType",
                false
        );
    }


    @Test
    public void testInvalidValues() throws IOException {
        TypeDescriptor base64BinaryObj = schema.get("base64BinaryObj");
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("base64Binaries").getItem().getItems()) {
            assertFalse(
                base64BinaryObj.validate(itemWrapper, false)
            );
        }
    }
}
