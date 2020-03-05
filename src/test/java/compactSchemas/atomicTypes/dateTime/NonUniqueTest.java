package compactSchemas.atomicTypes.dateTime;

import base.BaseTest;
import jsound.typedescriptors.object.FieldDescriptor;
import org.api.executors.JSoundExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NonUniqueTest extends BaseTest {
    private static String filePath = "atomicTypes/dateTime/nonUniqueError1.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> dateTimeObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/dateTime/dateTimeSchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        dateTimeObj = schema.get("dateTimeObj").getFacets().getObjectContent();
    }


    @Test
    public void testUniqueField() throws IOException {
        assertTrue(schema.get("dateTimeObj").isObjectType());
        assertTrue(dateTimeObj.get("uniqueDateTime").isUnique());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));

        assertEquals(
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("dateTimes")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueDateTime")
                .getItem(),
            jSoundSchema.instanceItem.getItem()
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
        assertFalse(schema.get("arrayOfDateTimes").validate(jSoundSchema.instanceItem.getItem().getItemMap().get("dateTimes"), false));

        filePath = "atomicTypes/dateTime/nonUniqueError2.json";
        initializeApplication();
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        assertEquals(
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("dateTimes")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueDateTime")
                .getItem(),
            jSoundSchema.instanceItem.getItem()
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
        assertFalse(schema.get("arrayOfDateTimes").validate(jSoundSchema.instanceItem.getItem().getItemMap().get("dateTimes"), false));

        filePath = "atomicTypes/dateTime/nonUniqueError3.json";
        initializeApplication();
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        assertEquals(
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("dateTimes")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueDateTime")
                .getItem(),
            jSoundSchema.instanceItem.getItem()
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
        assertFalse(schema.get("arrayOfDateTimes").validate(jSoundSchema.instanceItem.getItem().getItemMap().get("dateTimes"), false));
    }
}
