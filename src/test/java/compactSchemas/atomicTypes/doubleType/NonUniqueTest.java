package compactSchemas.atomicTypes.doubleType;

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
    private static final String filePath = "atomicTypes/double/nonUniqueError.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> doubleObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/double/doubleSchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        doubleObj = schema.get("doubleObj").getFacets().getObjectContent();
    }


    @Test
    public void testUniqueField() throws IOException {
        assertTrue(schema.get("doubleObj").isObjectType());
        assertTrue(doubleObj.get("uniqueDouble").isUnique());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        assertEquals(
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("doubles")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueDouble")
                .getItem(),
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("doubles")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueDouble")
                .getItem()
        );
        assertFalse(schema.get("arrayOfDoubles").validate(jSoundSchema.instanceItem.getItem().getItemMap().get("doubles"), false));
    }
}
