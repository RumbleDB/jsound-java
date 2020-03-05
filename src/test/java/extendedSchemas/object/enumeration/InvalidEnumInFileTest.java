package extendedSchemas.object.enumeration;

import base.BaseTest;
import jsound.exceptions.UnexpectedTypeException;
import org.api.Item;
import org.api.ItemWrapper;
import org.api.TypeDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.api.executors.JSoundExecutor;
import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvalidEnumInFileTest extends BaseTest {
    private static TypeDescriptor objectObj;
    String filePath = "object/enumeration/invalidEnumError.json";
    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/object/enumeration/enumerationSchema.json",
                "targetType",
                false
        );
        objectObj = schema.get("objectObj");
    }

    @Test
    public void testInvalidValues() throws IOException {
        assertTrue(objectObj.isObjectType());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("objects").getItem().getItems()) {
            assertFalse(
                objectObj.validate(itemWrapper, false)
            );
        }
        List<Item> enumValues = schema.get("objectType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("objects").getItem().getItems()) {
            try {
                assertFalse(
                    enumValues.contains(itemWrapper.getItem().getItemMap().get("myObject").getItem())
                );
            } catch (UnexpectedTypeException ignored) {
            }
        }
    }
}
