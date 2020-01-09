package compactSchemas.atomicTypes.doubleType;

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
    private static final String filePath = "atomicTypes/double/nonUniqueError.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> doubleObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/double/doubleSchema.json";
        BaseTest.initializeApplication(
            (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            filePath,
            compact
        );
        doubleObj = schema.get("doubleObj").getFacets().getObjectContent();
    }


    @Test
    public void testUniqueField() {
        assertTrue(schema.get("doubleObj").isObjectType());
        assertTrue(doubleObj.get("uniqueDouble").isUnique());
        assertFalse(schemaItem.validate(fileItem, false));
        assertEquals(
            fileItem.getItem()
                .getItemMap()
                .get("doubles")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueDouble")
                .getItem(),
            fileItem.getItem()
                .getItemMap()
                .get("doubles")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueDouble")
                .getItem()
        );
        assertFalse(schema.get("arrayOfDoubles").validate(fileItem.getItem().getItemMap().get("doubles"), false));
    }
}
