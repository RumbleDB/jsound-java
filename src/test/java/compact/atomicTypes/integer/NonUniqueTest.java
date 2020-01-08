package compact.atomicTypes.integer;

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
    public static final String filePath = "atomicTypes/integer/nonUniqueError.json";
    protected static String schemaPath = "atomicTypes/integerSchema.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> integerObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
                (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
                filePath,
                compact
        );
        integerObj = schema.get("integerObj").getFacets().getObjectContent();
    }


    @Test
    public void testUniqueField() {
        assertTrue(schema.get("integerObj").isObjectType());
        assertTrue(integerObj.get("uniqueInteger").isUnique());
        assertFalse(schemaItem.validate(fileItem, false));
        assertEquals(
            fileItem.getItem()
                .getItemMap()
                .get("integers")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueInteger")
                .getItem(),
            fileItem.getItem()
                .getItemMap()
                .get("integers")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueInteger")
                .getItem()
        );
        assertFalse(schema.get("arrayOfIntegers").validate(fileItem.getItem().getItemMap().get("integers"), false));
    }
}
