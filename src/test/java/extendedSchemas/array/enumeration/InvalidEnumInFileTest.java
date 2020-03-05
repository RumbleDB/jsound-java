package extendedSchemas.array.enumeration;

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
    String filePath = "array/enumeration/invalidEnumError.json";
    private static TypeDescriptor arrayObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
            schemaPathPrefix + "extendedSchemas/array/enumerationSchema.json",
            "targetType",
            false
        );
        arrayObj = schema.get("arrayObj");
    }

    @Test
    public void testInvalidValues() throws IOException {
        assertTrue(arrayObj.isObjectType());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));

        for (
            ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("arrays")
                .getItem()
                .getItems()
        ) {
            assertFalse(
                arrayObj.validate(itemWrapper, false)
            );
        }
        List<Item> enumValues = schema.get("arrayType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        for (
            ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem()
                .getItemMap()
                .get("arrays")
                .getItem()
                .getItems()
        ) {
            try {
                assertFalse(
                    enumValues.contains(itemWrapper.getItem().getItemMap().get("myArray").getItem())
                );
            } catch (UnexpectedTypeException ignored) {
            }
        }
    }
}
