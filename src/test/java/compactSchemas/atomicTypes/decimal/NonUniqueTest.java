package compactSchemas.atomicTypes.decimal;

import base.BaseTest;
import jsound.typedescriptors.object.FieldDescriptor;
import org.api.executors.JSoundExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.api.executors.JSoundExecutor.schema;
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
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        decimalObj = schema.get("decimalObj").getFacets().getObjectContent();
    }


    @Test
    public void testUniqueField() throws IOException {
        assertTrue(schema.get("decimalObj").isObjectType());
        assertTrue(decimalObj.get("uniqueDecimal").isUnique());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        assertEquals(
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("decimals")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueDecimal")
                .getItem(),
            jSoundSchema.instanceItem.getItem()
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
        assertFalse(schema.get("arrayOfDecimals").validate(jSoundSchema.instanceItem.getItem().getItemMap().get("decimals"), false));
    }
}
