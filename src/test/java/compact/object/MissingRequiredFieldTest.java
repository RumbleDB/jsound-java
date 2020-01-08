package compact.object;

import jsound.typedescriptors.object.FieldDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;
import base.BaseTest;

import java.io.IOException;
import java.util.Map;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MissingRequiredFieldTest extends BaseTest {
    public static final String filePath = "object/missingRequiredFieldError.json";
    protected static String schemaPath = "object/missingRequiredFieldSchema.json";
    protected static boolean compact = true;
    private static Map<String, FieldDescriptor> object;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
                (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
                filePath,
                compact
        );
        object = schema.get("object").getFacets().getObjectContent();
    }

    @Test
    public void testMissingRequiredField() {
        assertTrue(schema.get("object").isObjectType());
        assertTrue(object.get("requiredField").isRequired());
        assertFalse(schemaItem.validate(fileItem, false));
        assertTrue(
            !fileItem.getItem()
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
                .validate(fileItem.getItem().getItemMap().get("objects").getItem().getItems().get(0), false)
        );
    }
}
