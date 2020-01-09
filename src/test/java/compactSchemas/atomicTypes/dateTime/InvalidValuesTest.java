package compactSchemas.atomicTypes.dateTime;

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
    private static final String filePath = "atomicTypes/dateTime/invalidValuesError.json";
    protected static boolean compact = true;
    private static TypeDescriptor dateTimeObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/dateTime/dateTimeSchema.json";
        BaseTest.initializeApplication(
            (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            filePath,
            compact
        );
        dateTimeObj = schema.get("dateTimeObj");
    }

    @Test
    public void testInvalidValues() {
        assertTrue(dateTimeObj.isObjectType());
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("dateTimes").getItem().getItems()) {
            assertFalse(
                dateTimeObj.validate(itemWrapper, false)
            );
        }
    }
}
