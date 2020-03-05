package extendedSchemas.atomicTypes.dateTime.enumeration;

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
    private static TypeDescriptor dateTimeObj;
    String filePath = "atomicTypes/dateTime/enumeration/invalidEnumError.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/dateTime/enumerationSchema.json",
                "targetType",
                false
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
        List<Item> enumValues = schema.get("dateTimeType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("dateTimes").getItem().getItems()) {
            try {
                assertFalse(
                    enumValues.contains(itemWrapper.getItem().getItemMap().get("myDateTime").getItem())
                );
            } catch (UnexpectedTypeException ignored) {
            }
        }
    }
}
