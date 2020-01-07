package parsing.compact.atomicTypes.nullType;

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
    static String filePath = "src/main/resources/compact/atomicTypes/null/nonUniqueError.json";
    static String schemaPath = "src/main/resources/compact/atomicTypes/null/nullSchema.json";
    static String rootType = "rootType";
    public static boolean compact = true;

    private static Map<String, FieldDescriptor> nullObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(schemaPath, filePath, rootType, compact);
        nullObj = schema.get("nullObj").getFacets().getObjectContent();
    }

    @Test
    public void testUniqueField() {
        assertTrue(schema.get("nullObj").isObjectType());
        assertTrue(nullObj.get("uniqueNull").isUnique());
        assertFalse(schemaItem.validate(fileItem, false));
        assertEquals(
                fileItem.getItem()
                        .getItemMap()
                        .get("nulls")
                        .getItem()
                        .getItems()
                        .get(0)
                        .getItem()
                        .getItemMap()
                        .get("uniqueNull")
                        .getItem(),
                fileItem.getItem()
                        .getItemMap()
                        .get("nulls")
                        .getItem()
                        .getItems()
                        .get(1)
                        .getItem()
                        .getItemMap()
                        .get("uniqueNull")
                        .getItem()
        );
        assertFalse(schema.get("arrayOfNulls").validate(fileItem.getItem().getItemMap().get("nulls"), false));
    }
}
