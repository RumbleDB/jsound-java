package compactSchemas.array;

import base.BaseTest;
import jsound.typedescriptors.object.FieldDescriptor;
import org.api.executors.JSoundExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NonUniqueTest extends BaseTest {
    private static final String filePath = "array/nonUniqueError.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> arrayObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "array/arraySchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        arrayObj = schema.get("arrayObj").getFacets().getObjectContent();
    }

    @Test
    public void testUniqueField() throws IOException {
        assertTrue(schema.get("arrayObj").isObjectType());
        assertTrue(arrayObj.get("uniqueArrayOfBinaries").isUnique());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        assertEquals(
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("arrays")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueArrayOfBinaries")
                .getItem(),
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("arrays")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueArrayOfBinaries")
                .getItem()
        );
        assertFalse(
            schema.get("arrayOfArrays").validate(jSoundSchema.instanceItem.getItem().getItemMap().get("arrays"), false)
        );
    }
}
