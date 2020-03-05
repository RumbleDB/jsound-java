package compactSchemas.atomicTypes.hexBinary;

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
    private static final String filePath = "atomicTypes/hexBinary/invalidValuesError.json";
    protected static boolean compact = true;
    private static TypeDescriptor hexBinaryObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/hexBinary/hexBinarySchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        hexBinaryObj = schema.get("hexBinaryObj");
    }

    @Test
    public void testInvalidValues() throws IOException {
        assertTrue(hexBinaryObj.isObjectType());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("hexBinaries").getItem().getItems()) {
            assertFalse(
                hexBinaryObj.validate(itemWrapper, false)
            );
        }
    }
}
