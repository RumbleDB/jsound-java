package compactSchemas.atomicTypes.decimal;

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
    private static final String filePath = "atomicTypes/decimal/nonUniqueError.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> decimalObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/decimal/decimalSchema.json";
        BaseTest.initializeApplication(
            (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            filePath,
            compact
        );
        decimalObj = schema.get("decimalObj").getFacets().getObjectContent();
    }


    @Test
    public void testUniqueField() {
        assertTrue(schema.get("decimalObj").isObjectType());
        assertTrue(decimalObj.get("uniqueDecimal").isUnique());
        assertFalse(schemaItem.validate(fileItem, false));
        assertEquals(
            fileItem.getItem()
                .getItemMap()
                .get("decimals")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueDecimal")
                .getItem(),
            fileItem.getItem()
                .getItemMap()
                .get("decimals")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueDecimal")
                .getItem()
        );
        assertFalse(schema.get("arrayOfDecimals").validate(fileItem.getItem().getItemMap().get("decimals"), false));
    }
}
