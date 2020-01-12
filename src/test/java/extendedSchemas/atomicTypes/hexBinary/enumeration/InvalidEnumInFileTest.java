package extendedSchemas.atomicTypes.hexBinary.enumeration;

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

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvalidEnumInFileTest extends BaseTest {
    private static TypeDescriptor hexBinaryObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
                "extendedSchemas/atomicTypes/hexBinary/enumerationSchema.json",
                "atomicTypes/hexBinary/enumeration/invalidEnumError.json",
                false
        );
        hexBinaryObj = schema.get("hexBinaryObj");
    }

    @Test
    public void testInvalidValues() {
        assertTrue(hexBinaryObj.isObjectType());
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("hexBinaries").getItem().getItems()) {
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
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("hexBinaries").getItem().getItems()) {
            try {
                assertFalse(
                    enumValues.contains(itemWrapper.getItem().getItemMap().get("myHexBinary").getItem())
                );
            } catch (UnexpectedTypeException ignored) {
            }
        }
    }
}
