package extendedSchemas.atomicTypes.base64Binary.enumeration;

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
    private static TypeDescriptor base64BinaryObj;
    String filePath = "atomicTypes/base64Binary/enumeration/invalidEnumError.json";
    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/base64Binary/enumerationSchema.json",
                "targetType",
                false
        );
        base64BinaryObj = schema.get("base64BinaryObj");
    }

    @Test
    public void testInvalidValues() throws IOException {
        assertTrue(base64BinaryObj.isObjectType());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("base64Binaries").getItem().getItems()) {
            assertFalse(
                base64BinaryObj.validate(itemWrapper, false)
            );
        }
        List<Item> enumValues = schema.get("base64BinaryType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("base64Binaries").getItem().getItems()) {
            try {
                assertFalse(
                    enumValues.contains(itemWrapper.getItem().getItemMap().get("myBase64Binary").getItem())
                );
            } catch (UnexpectedTypeException ignored) {
            }
        }
    }
}
