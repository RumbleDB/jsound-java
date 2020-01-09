package compactSchemas.atomicTypes.hexBinary;

import base.BaseTest;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvalidValuesTest extends BaseTest {
    private static final String filePath = "atomicTypes/hexBinary/invalidValuesError.json";
    protected static boolean compact = true;
    private static TypeDescriptor hexBinaryObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/hexBinary/hexBinarySchema.json";
        BaseTest.initializeApplication(
            (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            filePath,
            compact
        );
        hexBinaryObj = schema.get("hexBinaryObj");
    }

    @Test
    public void testInvalidValues() {
        assertTrue(hexBinaryObj.isObjectType());
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("hexBinaries").getItem().getItems()) {
            assertFalse(
                hexBinaryObj.validate(itemWrapper, false)
            );
        }
    }
}
