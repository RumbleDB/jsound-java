package compact.atomicTypes.doubleType;

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
    public static final String filePath = "atomicTypes/double/invalidValuesError.json";
    protected static String schemaPath = "atomicTypes/doubleSchema.json";
    protected static boolean compact = true;
    private static TypeDescriptor doubleObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
                (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
                filePath,
                compact
        );
        doubleObj = schema.get("doubleObj");
    }

    @Test
    public void testInvalidValues() {
        assertTrue(doubleObj.isObjectType());
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("doubles").getItem().getItems()) {
            assertFalse(
                doubleObj.validate(itemWrapper, false)
            );
        }
    }
}
