package compactSchemas.union;

import base.BaseTest;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import org.api.executors.JSoundExecutor;
import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvalidValuesTest extends BaseTest {
    private static final String filePath = "union/invalidValuesError.json";
    protected static boolean compact = true;
    private static TypeDescriptor unionObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "union/unionSchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        unionObj = schema.get("unionObj");
    }

    @Test
    public void testInvalidValues() throws IOException {
        assertTrue(unionObj.isObjectType());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("unions").getItem().getItems()) {
            assertFalse(
                unionObj.validate(itemWrapper, false)
            );
        }
    }
}
