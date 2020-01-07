package parsing.compact.union;

import org.api.ItemWrapper;
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
    static String filePath = "src/main/resources/compact/union/invalidValuesError.json";
    static String schemaPath = "src/main/resources/compact/union/unionSchema.json";
    static String rootType = "rootType";
    public static boolean compact = true;

    private static TypeDescriptor unionObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(schemaPath, filePath, rootType, compact);
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
