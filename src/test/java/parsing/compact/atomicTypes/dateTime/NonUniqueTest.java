package parsing.compact.atomicTypes.dateTime;

import jsound.typedescriptors.object.FieldDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;
import parsing.BaseTest;

import java.io.IOException;
import java.util.Map;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NonUniqueTest extends BaseTest {
    static String filePath = "src/main/resources/compact/atomicTypes/dateTime/nonUniqueError1.json";
    static String schemaPath = "src/main/resources/compact/atomicTypes/dateTime/dateTimeSchema.json";
    static String rootType = "rootType";
    public static boolean compact = true;

    private static Map<String, FieldDescriptor> dateTimeObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(schemaPath, filePath, rootType, compact);
        dateTimeObj = schema.get("dateTimeObj").getFacets().getObjectContent();
    }

    @Test
    public void testUniqueField() throws IOException {
        assertTrue(schema.get("dateTimeObj").isObjectType());
        assertTrue(dateTimeObj.get("uniqueDateTime").isUnique());
        assertFalse(schemaItem.validate(fileItem, false));

        assertEquals(
                fileItem.getItem()
                        .getItemMap()
                        .get("dateTimes")
                        .getItem()
                        .getItems()
                        .get(0)
                        .getItem()
                        .getItemMap()
                        .get("uniqueDateTime")
                        .getItem(),
                fileItem.getItem()
                        .getItemMap()
                        .get("dateTimes")
                        .getItem()
                        .getItems()
                        .get(1)
                        .getItem()
                        .getItemMap()
                        .get("uniqueDateTime")
                        .getItem()
        );
        assertFalse(schema.get("arrayOfDateTimes").validate(fileItem.getItem().getItemMap().get("dateTimes"), false));

        filePath = "src/main/resources/compact/atomicTypes/dateTime/nonUniqueError2.json";
        initializeApplication();
        assertFalse(schemaItem.validate(fileItem, false));
        assertEquals(
                fileItem.getItem()
                        .getItemMap()
                        .get("dateTimes")
                        .getItem()
                        .getItems()
                        .get(0)
                        .getItem()
                        .getItemMap()
                        .get("uniqueDateTime")
                        .getItem(),
                fileItem.getItem()
                        .getItemMap()
                        .get("dateTimes")
                        .getItem()
                        .getItems()
                        .get(1)
                        .getItem()
                        .getItemMap()
                        .get("uniqueDateTime")
                        .getItem()
        );
        assertFalse(schema.get("arrayOfDateTimes").validate(fileItem.getItem().getItemMap().get("dateTimes"), false));

        filePath = "src/main/resources/compact/atomicTypes/dateTime/nonUniqueError3.json";
        initializeApplication();
        assertFalse(schemaItem.validate(fileItem, false));
        assertEquals(
                fileItem.getItem()
                        .getItemMap()
                        .get("dateTimes")
                        .getItem()
                        .getItems()
                        .get(0)
                        .getItem()
                        .getItemMap()
                        .get("uniqueDateTime")
                        .getItem(),
                fileItem.getItem()
                        .getItemMap()
                        .get("dateTimes")
                        .getItem()
                        .getItems()
                        .get(1)
                        .getItem()
                        .getItemMap()
                        .get("uniqueDateTime")
                        .getItem()
        );
        assertFalse(schema.get("arrayOfDateTimes").validate(fileItem.getItem().getItemMap().get("dateTimes"), false));
    }
}
