package compactSchemas.atomicTypes.nullType;

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
    public static final String filePath = "atomicTypes/null/invalidValuesError.json";
    protected static String schemaPath = "atomicTypes/nullSchema.json";
    protected static boolean compact = true;
    private static TypeDescriptor nullObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
            (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            filePath,
            compact
        );
        nullObj = schema.get("nullObj");
    }

    @Test
    public void testInvalidValues() {
        assertTrue(nullObj.isObjectType());
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("nulls").getItem().getItems()) {
            assertFalse(
                nullObj.validate(itemWrapper, false)
            );
        }
    }
}
