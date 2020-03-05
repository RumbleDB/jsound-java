package compactSchemas.atomicTypes.booleanType;

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
    private static final String filePath = "atomicTypes/boolean/nonUniqueError.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> booleanObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/boolean/booleanSchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        booleanObj = schema.get("booleanObj").getFacets().getObjectContent();
    }


    @Test
    public void testUniqueField() throws IOException {
        assertTrue(schema.get("booleanObj").isObjectType());
        assertTrue(booleanObj.get("uniqueBoolean").isUnique());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        assertEquals(
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("booleans")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .get("uniqueBoolean")
                .getItem(),
            jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("booleans")
                .getItem()
                .getItems()
                .get(1)
                .getItem()
                .getItemMap()
                .get("uniqueBoolean")
                .getItem()
        );
        assertFalse(schema.get("arrayOfBooleans").validate(jSoundSchema.instanceItem.getItem().getItemMap().get("booleans"), false));
    }
}
