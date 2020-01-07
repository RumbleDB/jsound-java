package parsing.compact.array;

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
    static String filePath = "src/main/resources/compact/array/nonUniqueError.json";
    static String schemaPath = "src/main/resources/compact/array/arraySchema.json";
    static String rootType = "rootType";
    public static boolean compact = true;

    private static Map<String, FieldDescriptor> arrayObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(schemaPath, filePath, rootType, compact);
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
