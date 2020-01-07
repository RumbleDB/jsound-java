package parsing.compact.atomicTypes.anyURI;

import org.api.TypeDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;
import parsing.BaseTest;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvalidValueTest extends BaseTest {
    static String filePath = "src/main/resources/compact/atomicTypes/anyURI/invalidValuesError.json";
    static String schemaPath = "src/main/resources/compact/atomicTypes/anyURI/anyURISchema.json";
    static String rootType = "rootType";
    public static boolean compact = true;

    private static TypeDescriptor anyURIObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(schemaPath, filePath, rootType, compact);
        anyURIObj = schema.get("anyURIObj");
    }

    @Test
    public void testInvalidValues() {
        assertTrue(anyURIObj.isObjectType());
        assertFalse(schemaItem.validate(fileItem, false));
        assertFalse(
            anyURIObj.validate(fileItem.getItem().getItemMap().get("anyURIs").getItem().getItems().get(0), false)
        );
        assertFalse(
            anyURIObj.validate(fileItem.getItem().getItemMap().get("anyURIs").getItem().getItems().get(1), false)
        );
    }
}
