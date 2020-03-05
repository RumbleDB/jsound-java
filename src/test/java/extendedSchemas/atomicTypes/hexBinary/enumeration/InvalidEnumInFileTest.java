package extendedSchemas.atomicTypes.hexBinary.enumeration;

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
    private static TypeDescriptor hexBinaryObj;
    String filePath = "atomicTypes/hexBinary/enumeration/invalidEnumError.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/hexBinary/enumerationSchema.json",
                "targetType",
                false
        );
        hexBinaryObj = schema.get("hexBinaryObj");
    }

    @Test
    public void testInvalidValues() throws IOException {
        assertTrue(hexBinaryObj.isObjectType());
        assertFalse(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("hexBinaries").getItem().getItems()) {
            assertFalse(
                hexBinaryObj.validate(itemWrapper, false)
            );
        }
        List<Item> enumValues = schema.get("hexBinaryType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("hexBinaries").getItem().getItems()) {
            try {
                assertFalse(
                    enumValues.contains(itemWrapper.getItem().getItemMap().get("myHexBinary").getItem())
                );
            } catch (UnexpectedTypeException ignored) {
            }
        }
    }
}
