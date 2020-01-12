package extendedSchemas.array.enumeration;

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
    private static TypeDescriptor arrayObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/array/enumerationSchema.json",
            "array/enumeration/invalidEnumError.json",
            false
        );
        arrayObj = schema.get("arrayObj");
    }

    @Test
    public void testInvalidValues() {
        assertTrue(arrayObj.isObjectType());
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("arrays").getItem().getItems()) {
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
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("arrays").getItem().getItems()) {
            try {
                assertFalse(
                    enumValues.contains(itemWrapper.getItem().getItemMap().get("myArray").getItem())
                );
            } catch (UnexpectedTypeException ignored) {
            }
        }
    }
}
