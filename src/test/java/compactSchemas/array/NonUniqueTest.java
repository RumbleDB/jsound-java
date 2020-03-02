package compactSchemas.array;

import base.BaseTest;
import jsound.typedescriptors.object.FieldDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NonUniqueTest extends BaseTest {
    private static final String filePath = "array/nonUniqueError.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> arrayObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "array/arraySchema.json";
        BaseTest.initializeApplication(
            (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            filePath,
            compact
        );
        arrayObj = schema.get("arrayObj").getFacets().getObjectContent();
    }

    @Test
    public void testUniqueField() {
        assertTrue(schema.get("arrayObj").isObjectType());
        assertTrue(arrayObj.get("uniqueArrayOfBinaries").isUnique());
        assertFalse(schemaItem.validate(fileItem, false));
        assertEquals(
            fileItem.getItem()
                .getItemMap()
                .get("arrays")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueArrayOfBinaries")
                .getItem(),
            fileItem.getItem()
                .getItemMap()
                .get("arrays")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueArrayOfBinaries")
                .getItem()
        );
        assertFalse(schema.get("arrayOfArrays").validate(fileItem.getItem().getItemMap().get("arrays"), false));
    }
}
