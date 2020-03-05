package compactSchemas.object;

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
    private static final String filePath = "object/invalidValuesError.json";
    protected static boolean compact = true;
    private static TypeDescriptor objectType;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "object/objectSchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        objectType = schema.get("objectType");
    }

    @Test
    public void testInvalidValues() throws IOException {
        assertTrue(objectType.isObjectType());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("objects").getItem().getItems()) {
            assertFalse(
                objectType.validate(itemWrapper, false)
            );
        }
    }
}
