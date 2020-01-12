package extendedSchemas.atomicTypes.duration.enumeration;

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
    private static TypeDescriptor durationObj;

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
                "extendedSchemas/atomicTypes/duration/enumerationSchema.json",
                "atomicTypes/duration/enumeration/invalidEnumError.json",
                false
        );
        durationObj = schema.get("durationObj");
    }

    @Test
    public void testInvalidValues() {
        assertTrue(durationObj.isObjectType());
        assertFalse(schemaItem.validate(fileItem, false));
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("durations").getItem().getItems()) {
            assertFalse(
                durationObj.validate(itemWrapper, false)
            );
        }
        List<Item> enumValues = schema.get("durationType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("durations").getItem().getItems()) {
            try {
                assertFalse(
                    enumValues.contains(itemWrapper.getItem().getItemMap().get("myDuration").getItem())
                );
            } catch (UnexpectedTypeException ignored) {
            }
        }
    }
}
