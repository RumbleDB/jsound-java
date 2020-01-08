package compact.atomicTypes.integer;

import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;
import base.BaseTest;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvalidValuesTest extends BaseTest {
    public static final String filePath = "atomicTypes/integer/invalidValuesError.json";
    protected static String schemaPath = "atomicTypes/integerSchema.json";
    protected static boolean compact = true;
    private static TypeDescriptor integerObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
                (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
                filePath,
                compact
        );
        integerObj = schema.get("integerObj");
    }

    @Test
    public void testInvalidValues() {
        assertTrue(integerObj.isObjectType());
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("integers").getItem().getItems()) {
            assertFalse(
                integerObj.validate(itemWrapper, false)
            );
        }
    }
}
