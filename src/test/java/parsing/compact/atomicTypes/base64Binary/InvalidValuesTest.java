package parsing.compact.atomicTypes.base64Binary;

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

public class InvalidValuesTest extends BaseTest {
    static String filePath = "src/main/resources/compact/atomicTypes/base64Binary/invalidValuesError.json";
    static String schemaPath = "src/main/resources/compact/atomicTypes/base64Binary/base64BinarySchema.json";
    static String rootType = "rootType";
    public static boolean compact = true;

    private static TypeDescriptor base64BinaryObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(schemaPath, filePath, rootType, compact);
        base64BinaryObj = schema.get("base64BinaryObj");
    }

    @Test
    public void testInvalidValues() {
        assertTrue(base64BinaryObj.isObjectType());
        assertFalse(schemaItem.validate(fileItem, false));
        assertFalse(
            base64BinaryObj.validate(
                fileItem.getItem().getItemMap().get("base64Binaries").getItem().getItems().get(0),
                false
            )
        );
        assertFalse(
            base64BinaryObj.validate(
                fileItem.getItem().getItemMap().get("base64Binaries").getItem().getItems().get(1),
                false
            )
        );
    }
}
