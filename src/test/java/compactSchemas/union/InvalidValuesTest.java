package compactSchemas.union;

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
    private static final String filePath = "union/invalidValuesError.json";
    protected static boolean compact = true;
    private static TypeDescriptor unionObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "union/unionSchema.json";
        BaseTest.initializeApplication(
            (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            filePath,
            compact
        );
        unionObj = schema.get("unionObj");
    }

    @Test
    public void testInvalidValues() {
        assertTrue(unionObj.isObjectType());
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("unions").getItem().getItems()) {
            assertFalse(
                unionObj.validate(itemWrapper, false)
            );
        }
    }
}
