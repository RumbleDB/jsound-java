package compactSchemas.atomicTypes.duration;

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
    public static final String filePath = "atomicTypes/duration/invalidValuesError.json";
    protected static String schemaPath = "atomicTypes/durationSchema.json";
    protected static boolean compact = true;
    private static TypeDescriptor durationObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
            (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            filePath,
            compact
        );
        durationObj = schema.get("durationObj");
    }

    @Test
    public void testInvalidValues() {
        assertTrue(durationObj.isObjectType());
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("durations").getItem().getItems()) {
            assertFalse(
                durationObj.validate(itemWrapper, false)
            );
        }
    }
}
