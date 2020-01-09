package compactSchemas.atomicTypes.anyURI;

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
    private static final String filePath = "atomicTypes/anyURI/nonUniqueError.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> anyURIObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/anyURI/anyURISchema.json";
        BaseTest.initializeApplication(
            (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            filePath,
            compact
        );
        anyURIObj = schema.get("anyURIObj").getFacets().getObjectContent();
    }

    @Test
    public void testUniqueField() {
        assertTrue(schema.get("anyURIObj").isObjectType());
        assertTrue(anyURIObj.get("uniqueAnyURI").isUnique());
        assertFalse(schemaItem.validate(fileItem, false));
        assertEquals(
            fileItem.getItem()
                .getItemMap()
                .get("anyURIs")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueAnyURI")
                .getItem(),
            fileItem.getItem()
                .getItemMap()
                .get("anyURIs")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueAnyURI")
                .getItem()
        );
        assertFalse(schema.get("arrayOfAnyURIs").validate(fileItem.getItem().getItemMap().get("anyURIs"), false));
    }
}
