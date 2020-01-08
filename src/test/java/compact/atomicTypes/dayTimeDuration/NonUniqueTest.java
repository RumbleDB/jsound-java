package compact.atomicTypes.dayTimeDuration;

import jsound.typedescriptors.object.FieldDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;
import base.BaseTest;

import java.io.IOException;
import java.util.Map;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NonUniqueTest extends BaseTest {
    public static final String filePath = "atomicTypes/dayTimeDuration/nonUniqueError.json";
    protected static String schemaPath = "atomicTypes/dayTimeDurationSchema.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> dayTimeDurationObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
                (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
                filePath,
                compact
        );
        dayTimeDurationObj = schema.get("dayTimeDurationObj").getFacets().getObjectContent();
    }


    @Test
    public void testUniqueField() {
        assertTrue(schema.get("dayTimeDurationObj").isObjectType());
        assertTrue(dayTimeDurationObj.get("uniqueDayTimeDuration").isUnique());
        assertFalse(schemaItem.validate(fileItem, false));
        assertEquals(
            fileItem.getItem()
                .getItemMap()
                .get("dayTimeDurations")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueDayTimeDuration")
                .getItem(),
            fileItem.getItem()
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
                .validate(fileItem.getItem().getItemMap().get("dayTimeDurations"), false)
        );
    }
}
