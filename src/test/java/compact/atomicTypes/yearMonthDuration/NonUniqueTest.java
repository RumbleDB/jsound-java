package compact.atomicTypes.yearMonthDuration;

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
    public static final String filePath = "atomicTypes/yearMonthDuration/nonUniqueError.json";
    protected static String schemaPath = "atomicTypes/yearMonthDurationSchema.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> yearMonthDurationObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
                (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
                filePath,
                compact
        );
        yearMonthDurationObj = schema.get("yearMonthDurationObj").getFacets().getObjectContent();
    }


    @Test
    public void testUniqueField() {
        assertTrue(schema.get("yearMonthDurationObj").isObjectType());
        assertTrue(yearMonthDurationObj.get("uniqueYearMonthDuration").isUnique());
        assertFalse(schemaItem.validate(fileItem, false));
        assertEquals(
            fileItem.getItem()
                .getItemMap()
                .get("yearMonthDurations")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueYearMonthDuration")
                .getItem(),
            fileItem.getItem()
                .getItemMap()
                .get("yearMonthDurations")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueYearMonthDuration")
                .getItem()
        );
        assertFalse(
            schema.get("arrayOfYearMonthDurations")
                .validate(fileItem.getItem().getItemMap().get("yearMonthDurations"), false)
        );
    }
}
