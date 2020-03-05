package compactSchemas.union;

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
    private static final String filePath = "union/nonUniqueError.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> unionObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "union/unionSchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        unionObj = schema.get("unionObj").getFacets().getObjectContent();
    }

    @Test
    public void testUniqueField() throws IOException {
        assertTrue(schema.get("unionObj").isObjectType());
        assertTrue(unionObj.get("uniqueDurations").isUnique());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        assertEquals(
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("unions")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueDurations")
                .getItem(),
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("unions")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueDurations")
                .getItem()
        );
        assertFalse(schema.get("arrayOfUnions").validate(jSoundSchema.instanceItem.getItem().getItemMap().get("unions"), false));
    }
}
