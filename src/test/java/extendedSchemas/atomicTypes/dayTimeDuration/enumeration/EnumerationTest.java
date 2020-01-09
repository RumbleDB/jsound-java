package extendedSchemas.atomicTypes.dayTimeDuration.enumeration;

import base.BaseTest;
import jsound.atomicItems.DayTimeDurationItem;
import jsound.types.ItemTypes;
import org.api.Item;
import org.api.ItemWrapper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
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
            "extendedSchemas/atomicTypes/dayTimeDuration/enumerationSchema.json",
            "atomicTypes/dayTimeDuration/enumeration/dayTimeDurationEnumeration.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("dayTimeDurationType").isDayTimeDurationType());
        assertTrue(schema.get("dayTimeDurationObj").isObjectType());
        assertTrue(
            schema.get("dayTimeDurationObj")
                .getFacets()
                .getObjectContent()
                .get("myDayTimeDuration")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isDayTimeDurationType()
        );
    }

    @Test
    public void testEnumeration() {
        List<DayTimeDurationItem> values = Arrays.asList(
                createDayTimeDurationItem("PT999999S"),
                createDayTimeDurationItem("PT1M30.5S"),
                createDayTimeDurationItem("P3DT99H66M4333.3S"),
                createDayTimeDurationItem("-P4DT5M")
        );
        List<Item> enumValues = schema.get("dayTimeDurationType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("dayTimeDurationType").getFacets().getEnumeration().size(), values.size());
        for (DayTimeDurationItem value : values) {
            assertTrue(enumValues.contains(value));
        }

        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("dayTimeDurations").getItem().getItems())
            assertTrue(values.contains((DayTimeDurationItem) itemWrapper.getItem().getItemMap().get("myDayTimeDuration").getItem()));
    }

    private DayTimeDurationItem createDayTimeDurationItem(String value) {
        return new DayTimeDurationItem(getDurationFromString(value, ItemTypes.DAYTIMEDURATION));
    }
    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
