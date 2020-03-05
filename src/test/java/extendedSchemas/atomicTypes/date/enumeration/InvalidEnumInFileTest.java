package extendedSchemas.atomicTypes.date.enumeration;

import base.BaseTest;
import jsound.exceptions.UnexpectedTypeException;
import org.api.Item;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import org.api.executors.JSoundExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvalidEnumInFileTest extends BaseTest {
    private static TypeDescriptor dateObj;
    String filePath = "atomicTypes/date/enumeration/invalidEnumError.json";
    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/date/enumerationSchema.json",
                "targetType",
                false
        );
        dateObj = schema.get("dateObj");
    }

    @Test
    public void testInvalidValues() throws IOException {
        assertTrue(dateObj.isObjectType());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("dates").getItem().getItems()) {
            assertFalse(
                dateObj.validate(itemWrapper, false)
            );
        }
        List<Item> enumValues = schema.get("dateType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("dates").getItem().getItems()) {
            try {
                assertFalse(
                    enumValues.contains(itemWrapper.getItem().getItemMap().get("myDate").getItem())
                );
            } catch (UnexpectedTypeException ignored) {
            }
        }
    }
}
