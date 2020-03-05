package extendedSchemas.atomicTypes.dateTime.enumeration;

import base.BaseTest;
import jsound.atomicItems.DateTimeItem;
import jsound.types.AtomicTypes;
import org.api.Item;
import org.api.ItemWrapper;
import org.api.executors.JSoundExecutor;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnumerationTest extends BaseTest {
    String filePath = "atomicTypes/dateTime/enumeration/dateTimeEnumeration.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/dateTime/enumerationSchema.json",
                "targetType",
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

        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("dateTimes").getItem().getItems())
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
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
