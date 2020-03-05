package compactSchemas.atomicTypes.dateTime;

import base.BaseTest;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import org.api.executors.JSoundExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvalidValuesTest extends BaseTest {
    private static final String filePath = "atomicTypes/dateTime/invalidValuesError.json";
    protected static boolean compact = true;
    private static TypeDescriptor dateTimeObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        String schemaPath = "atomicTypes/dateTime/dateTimeSchema.json";
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + (compact ? "compactSchemas/" : "extendedSchemas/") + schemaPath,
            "targetType",
            compact
        );
        dateTimeObj = schema.get("dateTimeObj");
    }

    @Test
    public void testInvalidValues() throws IOException {
        assertTrue(dateTimeObj.isObjectType());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("dateTimes").getItem().getItems()) {
            assertFalse(
                dateTimeObj.validate(itemWrapper, false)
            );
        }
    }
}
