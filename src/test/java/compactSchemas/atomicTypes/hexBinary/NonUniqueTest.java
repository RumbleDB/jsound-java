package compactSchemas.atomicTypes.hexBinary;

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
    private static final String filePath = "atomicTypes/hexBinary/nonUniqueError.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> hexBinaryObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/hexBinary/hexBinarySchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        hexBinaryObj = schema.get("hexBinaryObj").getFacets().getObjectContent();
    }


    @Test
    public void testUniqueField() throws IOException {
        assertTrue(schema.get("hexBinaryObj").isObjectType());
        assertTrue(hexBinaryObj.get("uniqueHexBinary").isUnique());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        assertEquals(
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("hexBinaries")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueHexBinary")
                .getItem(),
            jSoundSchema.instanceItem.getItem()
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
            schema.get("arrayOfHexBinaries").validate(jSoundSchema.instanceItem.getItem().getItemMap().get("hexBinaries"), false)
        );
    }
}
