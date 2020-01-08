package compact.atomicTypes.time;

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
    public static String filePath = "atomicTypes/time/nonUniqueError1.json";
    protected static String schemaPath = "atomicTypes/timeSchema.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> timeObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
                (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
                filePath,
                compact
        );
        timeObj = schema.get("timeObj").getFacets().getObjectContent();
    }


    @Test
    public void testUniqueField() throws IOException {
        assertTrue(schema.get("timeObj").isObjectType());
        assertTrue(timeObj.get("uniqueTime").isUnique());
        assertFalse(schemaItem.validate(fileItem, false));
        assertEquals(
            fileItem.getItem()
                .getItemMap()
                .get("times")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueTime")
                .getItem(),
            fileItem.getItem()
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
        assertFalse(schema.get("arrayOfTimes").validate(fileItem.getItem().getItemMap().get("times"), false));

        filePath = "atomicTypes/time/nonUniqueError2.json";
        initializeApplication();
        assertFalse(schemaItem.validate(fileItem, false));
        assertEquals(
            fileItem.getItem()
                .getItemMap()
                .get("times")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueTime")
                .getItem(),
            fileItem.getItem()
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
        assertFalse(schema.get("arrayOfTimes").validate(fileItem.getItem().getItemMap().get("times"), false));
    }
}
