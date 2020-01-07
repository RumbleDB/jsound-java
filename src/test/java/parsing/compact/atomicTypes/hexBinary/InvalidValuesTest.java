package parsing.compact.atomicTypes.hexBinary;

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
    static String filePath = "src/main/resources/compact/atomicTypes/hexBinary/invalidValuesError.json";
    static String schemaPath = "src/main/resources/compact/atomicTypes/hexBinary/hexBinarySchema.json";
    static String rootType = "rootType";
    public static boolean compact = true;

    private static TypeDescriptor hexBinaryObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(schemaPath, filePath, rootType, compact);
        hexBinaryObj = schema.get("hexBinaryObj");
    }

    @Test
    public void testInvalidValues() {
        assertTrue(hexBinaryObj.isObjectType());
        assertFalse(schemaItem.validate(fileItem, false));
        assertFalse(
            hexBinaryObj.validate(
                fileItem.getItem().getItemMap().get("hexBinaries").getItem().getItems().get(0),
                false
            )
        );
        assertFalse(
            hexBinaryObj.validate(
                fileItem.getItem().getItemMap().get("hexBinaries").getItem().getItems().get(1),
                false
            )
        );
    }
}
