package compactSchemas.atomicTypes.base64Binary;

import base.BaseTest;
import jsound.typedescriptors.object.FieldDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NonUniqueTest extends BaseTest {
    private static final String filePath = "atomicTypes/base64Binary/nonUniqueError.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> base64BinaryObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/base64Binary/base64BinarySchema.json";
        BaseTest.initializeApplication(
            (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            filePath,
            compact
        );
        base64BinaryObj = schema.get("base64BinaryObj").getFacets().getObjectContent();
    }

    @Test
    public void testUniqueField() {
        assertTrue(schema.get("base64BinaryObj").isObjectType());
        assertTrue(base64BinaryObj.get("uniqueBase64Binary").isUnique());
        assertFalse(schemaItem.validate(fileItem, false));
        assertEquals(
            fileItem.getItem()
                .getItemMap()
                .get("base64Binaries")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueBase64Binary")
                .getItem(),
            fileItem.getItem()
                .getItemMap()
                .get("base64Binaries")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueBase64Binary")
                .getItem()
        );
        assertFalse(
            schema.get("arrayOfBase64Binaries").validate(fileItem.getItem().getItemMap().get("base64Binaries"), false)
        );
    }
}
