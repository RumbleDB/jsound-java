package compactSchemas.atomicTypes.yearMonthDuration;

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
    private static final String filePath = "atomicTypes/yearMonthDuration/nonUniqueError.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> yearMonthDurationObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/yearMonthDuration/yearMonthDurationSchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        yearMonthDurationObj = schema.get("yearMonthDurationObj").getFacets().getObjectContent();
    }


    @Test
    public void testUniqueField() throws IOException {
        assertTrue(schema.get("yearMonthDurationObj").isObjectType());
        assertTrue(yearMonthDurationObj.get("uniqueYearMonthDuration").isUnique());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        assertEquals(
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("yearMonthDurations")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueYearMonthDuration")
                .getItem(),
            jSoundSchema.instanceItem.getItem()
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
                .validate(jSoundSchema.instanceItem.getItem().getItemMap().get("yearMonthDurations"), false)
        );
    }
}
