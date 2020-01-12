package extendedSchemas.atomicTypes.duration.enumeration;

import base.BaseTest;
import jsound.atomicItems.DurationItem;
import jsound.types.ItemTypes;
import org.api.Item;
import org.api.ItemWrapper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static jsound.atomicItems.DurationItem.getDurationFromString;
import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnumerationTest extends BaseTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/atomicTypes/duration/enumerationSchema.json",
            "atomicTypes/duration/enumeration/durationEnumeration.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("durationType").isDurationType());
        assertTrue(schema.get("durationObj").isObjectType());
        assertTrue(
            schema.get("durationObj")
                .getFacets()
                .getObjectContent()
                .get("myDuration")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isDurationType()
        );
    }

    @Test
    public void testEnumeration() {
        List<DurationItem> values = Arrays.asList(
            createDurationItem("P20M"),
            createDurationItem("PT20M"),
            createDurationItem("P2Y6M5DT12H35M30S"),
            createDurationItem("P4DT2H3M0S")
        );
        List<Item> enumValues = schema.get("durationType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("durationType").getFacets().getEnumeration().size(), values.size());
        for (DurationItem value : values) {
            assertTrue(enumValues.contains(value));
        }

        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("durations").getItem().getItems())
            assertTrue(values.contains((DurationItem) itemWrapper.getItem().getItemMap().get("myDuration").getItem()));
    }

    private DurationItem createDurationItem(String value) {
        return new DurationItem(getDurationFromString(value, ItemTypes.DURATION));
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
