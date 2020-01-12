package extendedSchemas.atomicTypes.yearMonthDuration.enumeration;

import base.BaseTest;
import jsound.atomicItems.YearMonthDurationItem;
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
            "extendedSchemas/atomicTypes/yearMonthDuration/enumerationSchema.json",
            "atomicTypes/yearMonthDuration/enumeration/yearMonthDurationEnumeration.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("yearMonthDurationType").isYearMonthDurationType());
        assertTrue(schema.get("yearMonthDurationObj").isObjectType());
        assertTrue(
            schema.get("yearMonthDurationObj")
                .getFacets()
                .getObjectContent()
                .get("myYearMonthDuration")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isYearMonthDurationType()
        );
    }

    @Test
    public void testEnumeration() {
        List<YearMonthDurationItem> values = Arrays.asList(
            createYearMonthDurationItem("-P33Y3M"),
            createYearMonthDurationItem("P999M"),
            createYearMonthDurationItem("P1Y3M"),
            createYearMonthDurationItem("P0Y")
        );
        List<Item> enumValues = schema.get("yearMonthDurationType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("yearMonthDurationType").getFacets().getEnumeration().size(), values.size());
        for (YearMonthDurationItem value : values) {
            assertTrue(enumValues.contains(value));
        }

        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("yearMonthDurations").getItem().getItems())
            assertTrue(
                values.contains(
                    (YearMonthDurationItem) itemWrapper.getItem().getItemMap().get("myYearMonthDuration").getItem()
                )
            );
    }

    private YearMonthDurationItem createYearMonthDurationItem(String value) {
        return new YearMonthDurationItem(getDurationFromString(value, ItemTypes.YEARMONTHDURATION));
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
