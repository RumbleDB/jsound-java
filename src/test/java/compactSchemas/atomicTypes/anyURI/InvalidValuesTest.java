package compactSchemas.atomicTypes.anyURI;

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
    private static final String filePath = "atomicTypes/anyURI/invalidValuesError.json";
    protected static boolean compact = true;
    private static TypeDescriptor anyURIObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/anyURI/anyURISchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        anyURIObj = schema.get("anyURIObj");
    }

    @Test
    public void testInvalidValues() throws IOException {
        assertTrue(anyURIObj.isObjectType());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("anyURIs").getItem().getItems()) {
            assertFalse(
                anyURIObj.validate(itemWrapper, false)
            );
        }
    }
}
