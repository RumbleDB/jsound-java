package compact.union;

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
    public static final String filePath = "union/nonUniqueError.json";
    protected static String schemaPath = "union/unionSchema.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> unionObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
                (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
                filePath,
                compact
        );
        unionObj = schema.get("unionObj").getFacets().getObjectContent();
    }

    @Test
    public void testUniqueField() {
        assertTrue(schema.get("unionObj").isObjectType());
        assertTrue(unionObj.get("uniqueDurations").isUnique());
        assertFalse(schemaItem.validate(fileItem, false));
        assertEquals(
            fileItem.getItem()
                .getItemMap()
                .get("unions")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueDurations")
                .getItem(),
            fileItem.getItem()
                .getItemMap()
                .get("unions")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueDurations")
                .getItem()
        );
        assertFalse(schema.get("arrayOfUnions").validate(fileItem.getItem().getItemMap().get("unions"), false));
    }
}
