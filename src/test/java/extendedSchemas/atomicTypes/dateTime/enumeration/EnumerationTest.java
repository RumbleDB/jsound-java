package extendedSchemas.atomicTypes.dateTime.enumeration;

import base.BaseTest;
import jsound.atomicItems.DateTimeItem;
import jsound.types.AtomicTypes;
import org.api.Item;
import org.api.ItemWrapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.api.executors.JSoundExecutor.fileItem;
import static org.api.executors.JSoundExecutor.schema;
import static org.api.executors.JSoundExecutor.schemaItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnumerationTest extends BaseTest {

    @BeforeClass
    public static void initializeApplication() throws IOException {
        BaseTest.initializeApplication(
            "extendedSchemas/atomicTypes/dateTime/enumerationSchema.json",
            "atomicTypes/dateTime/enumeration/dateTimeEnumeration.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("dateTimeType").isDateTimeType());
        assertTrue(schema.get("dateTimeObj").isObjectType());
        assertTrue(
            schema.get("dateTimeObj")
                .getFacets()
                .getObjectContent()
                .get("myDateTime")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isDateTimeType()
        );
    }

    @Test
    public void testEnumeration() {
        List<DateTimeItem> values = Arrays.asList(
            createDateTime("2004-04-12T13:20:00Z"),
            createDateTime("2004-04-12T13:20:00+14:00"),
            createDateTime("2004-04-12T13:20:15.5"),
            createDateTime("2001-12-12T24:00:00")
        );
        List<Item> enumValues = schema.get("dateTimeType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("dateTimeType").getFacets().getEnumeration().size(), values.size());
        for (DateTimeItem value : values) {
            assertTrue(enumValues.contains(value));
        }

        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("dateTimes").getItem().getItems())
            assertTrue(values.contains((DateTimeItem) itemWrapper.getItem().getItemMap().get("myDateTime").getItem()));
    }

    private DateTimeItem createDateTime(String value) {
        DateTime date = DateTimeItem.parseDateTime(value, AtomicTypes.DATETIME);
        if (!value.endsWith("Z") && date.getZone() == DateTimeZone.getDefault()) {
            return new DateTimeItem(date.withZoneRetainFields(DateTimeZone.UTC), false);
        }
        return new DateTimeItem(date, true);
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
