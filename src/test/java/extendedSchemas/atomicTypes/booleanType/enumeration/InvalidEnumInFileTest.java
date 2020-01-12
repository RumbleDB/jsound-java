package extendedSchemas.atomicTypes.booleanType.enumeration;

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
    private static TypeDescriptor booleanObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/atomicTypes/boolean/enumerationSchema.json",
            "atomicTypes/boolean/enumeration/invalidEnumError.json",
            false
        );
        booleanObj = schema.get("booleanObj");
    }

    @Test
    public void testInvalidValues() {
        assertTrue(booleanObj.isObjectType());
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("booleans").getItem().getItems()) {
            assertFalse(
                booleanObj.validate(itemWrapper, false)
            );
        }
        List<Item> enumValues = schema.get("booleanType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("booleans").getItem().getItems()) {
            try {
                assertFalse(
                    enumValues.contains(itemWrapper.getItem().getItemMap().get("myBoolean").getItem())
                );
            } catch (UnexpectedTypeException ignored) {
            }
        }
    }
}
