package parsing.compact.atomicTypes.hexBinary;

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
    static String filePath = "src/main/resources/compact/atomicTypes/hexBinary/nonUniqueError.json";
    static String schemaPath = "src/main/resources/compact/atomicTypes/hexBinary/hexBinarySchema.json";
    static String rootType = "rootType";
    public static boolean compact = true;

    private static Map<String, FieldDescriptor> hexBinaryObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(schemaPath, filePath, rootType, compact);
        hexBinaryObj = schema.get("hexBinaryObj").getFacets().getObjectContent();
    }

    @Test
    public void testUniqueField() {
        assertTrue(schema.get("hexBinaryObj").isObjectType());
        assertTrue(hexBinaryObj.get("uniqueHexBinary").isUnique());
        assertFalse(schemaItem.validate(fileItem, false));
        assertEquals(
            fileItem.getItem()
                .getItemMap()
                .get("hexBinaries")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueHexBinary")
                .getItem(),
            fileItem.getItem()
                .getItemMap()
                .get("hexBinaries")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueHexBinary")
                .getItem()
        );
        assertFalse(
            schema.get("arrayOfHexBinaries").validate(fileItem.getItem().getItemMap().get("hexBinaries"), false)
        );
    }
}
