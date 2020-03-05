package compactSchemas.object;

import base.BaseTest;
import jsound.typedescriptors.object.FieldDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import org.api.executors.JSoundExecutor;
import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NonUniqueTest extends BaseTest {
    private static final String filePath = "object/nonUniqueError.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> objectType;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "object/objectSchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        objectType = schema.get("objectType").getFacets().getObjectContent();
    }

    @Test
    public void testUniqueField() throws IOException {
        assertTrue(schema.get("objectType").isObjectType());
        assertTrue(objectType.get("uniqueObject").isUnique());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        assertEquals(
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("objects")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueObject")
                .getItem(),
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("objects")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueObject")
                .getItem()
        );
        assertFalse(schema.get("arrayOfObjects").validate(jSoundSchema.instanceItem.getItem().getItemMap().get("objects"), false));
    }
}
