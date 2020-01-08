package compact.atomicTypes.date;

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
    public static final String filePath = "atomicTypes/date/nonUniqueError.json";
    protected static String schemaPath = "atomicTypes/dateSchema.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> dateObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
                (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
                filePath,
                compact
        );
        dateObj = schema.get("dateObj").getFacets().getObjectContent();
    }

    @Test
    public void testUniqueField() {
        assertTrue(schema.get("dateObj").isObjectType());
        assertTrue(dateObj.get("uniqueDate").isUnique());
        assertFalse(schemaItem.validate(fileItem, false));
        assertEquals(
            fileItem.getItem()
                .getItemMap()
                .get("dates")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueDate")
                .getItem(),
            fileItem.getItem()
                .getItemMap()
                .get("dates")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueDate")
                .getItem()
        );
        assertFalse(schema.get("arrayOfDates").validate(fileItem.getItem().getItemMap().get("dates"), false));
    }
}
