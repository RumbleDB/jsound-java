package compactSchemas.array;

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
    private static final String filePath = "array/invalidValuesError.json";
    protected static boolean compact = true;
    private static TypeDescriptor arrayObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "array/arraySchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        arrayObj = schema.get("arrayObj");
    }

    @Test
    public void testInvalidValues() throws IOException {
        assertTrue(arrayObj.isObjectType());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        for (
            ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("arrays")
                .getItem()
                .getItems()
        ) {
            assertFalse(
                arrayObj.validate(itemWrapper, false)
            );
        }
    }
}
