package compactSchemas.atomicTypes.hexBinary;

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
    private static final String filePath = "atomicTypes/hexBinary/nonUniqueError.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> hexBinaryObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/hexBinary/hexBinarySchema.json";
        BaseTest.initializeApplication(
            (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            filePath,
            compact
        );
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
