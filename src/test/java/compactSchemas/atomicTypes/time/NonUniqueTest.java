package compactSchemas.atomicTypes.time;

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
    private static String filePath = "atomicTypes/time/nonUniqueError1.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> timeObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/time/timeSchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        timeObj = schema.get("timeObj").getFacets().getObjectContent();
    }


    @Test
    public void testUniqueField() throws IOException {
        assertTrue(schema.get("timeObj").isObjectType());
        assertTrue(timeObj.get("uniqueTime").isUnique());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        assertEquals(
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("times")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueTime")
                .getItem(),
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("times")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueTime")
                .getItem()
        );
        assertFalse(schema.get("arrayOfTimes").validate(jSoundSchema.instanceItem.getItem().getItemMap().get("times"), false));

        filePath = "atomicTypes/time/nonUniqueError2.json";
        initializeApplication();
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        assertEquals(
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("times")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueTime")
                .getItem(),
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("times")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueTime")
                .getItem()
        );
        assertFalse(schema.get("arrayOfTimes").validate(jSoundSchema.instanceItem.getItem().getItemMap().get("times"), false));
    }
}
