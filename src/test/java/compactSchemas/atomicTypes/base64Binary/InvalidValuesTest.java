package compactSchemas.atomicTypes.base64Binary;

import base.BaseTest;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import org.api.executors.JSoundExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvalidValuesTest extends BaseTest {
    private static final String filePath = "atomicTypes/base64Binary/invalidValuesError.json";
    protected static boolean compact = true;
    private static TypeDescriptor base64BinaryObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/base64Binary/base64BinarySchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact);
        base64BinaryObj = schema.get("base64BinaryObj");
    }

    @Test
    public void testInvalidValues() throws IOException {
        assertTrue(base64BinaryObj.isObjectType());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("base64Binaries").getItem().getItems()) {
            assertFalse(
                base64BinaryObj.validate(itemWrapper, false)
            );
        }
    }
}
