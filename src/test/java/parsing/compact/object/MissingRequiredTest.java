package parsing.compact.object;

import jsound.item.ObjectItem;
import jsound.typedescriptors.object.FieldDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;
import parsing.BaseTest;

import java.io.IOException;
import java.util.Map;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MissingRequiredTest extends BaseTest {
    static String filePath = "src/main/resources/compact/object/missingRequiredField/missingRequiredFieldError.json";
    static String schemaPath = "src/main/resources/compact/object/missingRequiredField/missingRequiredSchema.json";
    static String rootType = "rootType";
    public static boolean compact = true;

    private static Map<String, FieldDescriptor> object;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(schemaPath, filePath, rootType, compact);
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
