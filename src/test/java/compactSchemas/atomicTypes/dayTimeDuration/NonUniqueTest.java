package compactSchemas.atomicTypes.dayTimeDuration;

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
    private static final String filePath = "atomicTypes/dayTimeDuration/nonUniqueError.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> dayTimeDurationObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/dayTimeDuration/dayTimeDurationSchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        dayTimeDurationObj = schema.get("dayTimeDurationObj").getFacets().getObjectContent();
    }


    @Test
    public void testUniqueField() throws IOException {
        assertTrue(schema.get("dayTimeDurationObj").isObjectType());
        assertTrue(dayTimeDurationObj.get("uniqueDayTimeDuration").isUnique());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        assertEquals(
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("dayTimeDurations")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueDayTimeDuration")
                .getItem(),
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("dayTimeDurations")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueDayTimeDuration")
                .getItem()
        );
        assertFalse(
            schema.get("arrayOfDayTimeDurations")
                .validate(jSoundSchema.instanceItem.getItem().getItemMap().get("dayTimeDurations"), false)
        );
    }
}
