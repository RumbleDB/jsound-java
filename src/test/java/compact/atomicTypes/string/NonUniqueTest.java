package compact.atomicTypes.string;

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
    public static final String filePath = "atomicTypes/string/nonUniqueError.json";
    protected static String schemaPath = "atomicTypes/stringSchema.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> stringObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
                (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
                filePath,
                compact
        );
        stringObj = schema.get("stringObj").getFacets().getObjectContent();
    }


    @Test
    public void testUniqueField() {
        assertTrue(schema.get("stringObj").isObjectType());
        assertTrue(stringObj.get("uniqueString").isUnique());
        assertFalse(schemaItem.validate(fileItem, false));
        assertEquals(
            fileItem.getItem()
                .getItemMap()
                .get("strings")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueString")
                .getItem(),
            fileItem.getItem()
                .getItemMap()
                .get("strings")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueString")
                .getItem()
        );
        assertFalse(
            schema.get("arrayOfStrings").validate(fileItem.getItem().getItemMap().get("strings"), false)
        );
    }
}
