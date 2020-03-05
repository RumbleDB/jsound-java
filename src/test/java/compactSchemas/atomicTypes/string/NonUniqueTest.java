package compactSchemas.atomicTypes.string;

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
    private static final String filePath = "atomicTypes/string/nonUniqueError.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> stringObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/string/stringSchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        stringObj = schema.get("stringObj").getFacets().getObjectContent();
    }


    @Test
    public void testUniqueField() throws IOException {
        assertTrue(schema.get("stringObj").isObjectType());
        assertTrue(stringObj.get("uniqueString").isUnique());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        assertEquals(
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("strings")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueString")
                .getItem(),
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("strings")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueString")
                .getItem()
        );
        assertFalse(
            schema.get("arrayOfStrings").validate(jSoundSchema.instanceItem.getItem().getItemMap().get("strings"), false)
        );
    }
}
