package parsing.compact.array;

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
    static String filePath = "src/main/resources/compact/array/invalidValuesError.json";
    static String schemaPath = "src/main/resources/compact/array/arraySchema.json";
    static String rootType = "rootType";
    public static boolean compact = true;

    private static TypeDescriptor arrayObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(schemaPath, filePath, rootType, compact);
        arrayObj = schema.get("arrayObj");
    }

    @Test
    public void testInvalidValues() {
        assertTrue(arrayObj.isObjectType());
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("arrays").getItem().getItems()) {
            assertFalse(
                arrayObj.validate(itemWrapper, false)
            );
        }
    }
}
