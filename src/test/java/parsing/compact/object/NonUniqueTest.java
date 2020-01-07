package parsing.compact.object;

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
    static String filePath = "src/main/resources/compact/object/nonUniqueError.json";
    static String schemaPath = "src/main/resources/compact/object/objectSchema.json";
    static String rootType = "rootType";
    public static boolean compact = true;

    private static Map<String, FieldDescriptor> objectType;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(schemaPath, filePath, rootType, compact);
        objectType = schema.get("objectType").getFacets().getObjectContent();
    }

    @Test
    public void testUniqueField() {
        assertTrue(schema.get("objectType").isObjectType());
        assertTrue(objectType.get("uniqueObject").isUnique());
        assertFalse(schemaItem.validate(fileItem, false));
        assertEquals(
            fileItem.getItem()
                .getItemMap()
                .get("objects")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueObject")
                .getItem(),
            fileItem.getItem()
                .getItemMap()
                .get("objects")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueObject")
                .getItem()
        );
        assertFalse(schema.get("arrayOfObjects").validate(fileItem.getItem().getItemMap().get("objects"), false));
    }
}
