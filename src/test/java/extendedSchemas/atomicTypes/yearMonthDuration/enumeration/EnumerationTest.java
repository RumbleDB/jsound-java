package extendedSchemas.atomicTypes.yearMonthDuration.enumeration;

import base.BaseTest;
import jsound.atomicItems.YearMonthDurationItem;
import jsound.types.ItemTypes;
import org.api.Item;
import org.api.ItemWrapper;
import org.api.executors.JSoundExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static jsound.atomicItems.DurationItem.getDurationFromString;
import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnumerationTest extends BaseTest {
    String filePath = "atomicTypes/yearMonthDuration/enumeration/yearMonthDurationEnumeration.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/yearMonthDuration/enumerationSchema.json",
                "targetType",
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

        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("yearMonthDurations").getItem().getItems())
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
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
