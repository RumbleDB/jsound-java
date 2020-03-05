package compactSchemas.object;

import base.BaseTest;
import jsound.typedescriptors.object.FieldDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import org.api.executors.JSoundExecutor;
import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MissingRequiredFieldTest extends BaseTest {
    private static final String filePath = "object/missingRequiredFieldError.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> object;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "object/missingRequiredFieldSchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        object = schema.get("object").getFacets().getObjectContent();
    }

    @Test
    public void testMissingRequiredField() throws IOException {
        assertTrue(schema.get("object").isObjectType());
        assertTrue(object.get("requiredField").isRequired());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        assertTrue(
            !jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("objects")
                .getItem()
                .getItems()
                .get(0)
                .getItem()
                .getItemMap()
                .containsKey("requiredField")
                && object.get("requiredField").getDefaultValue() == null
        );
        assertFalse(
            schema.get("object")
                .validate(jSoundSchema.instanceItem.getItem().getItemMap().get("objects").getItem().getItems().get(0), false)
        );
    }
}
