package extendedSchemas.atomicTypes.date.enumeration;

import base.BaseTest;
import jsound.atomicItems.DateItem;
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
    String filePath = "atomicTypes/date/enumeration/dateEnumeration.json";
    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/date/enumerationSchema.json",
                "targetType",
                false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("dateType").isDateType());
        assertTrue(schema.get("dateObj").isObjectType());
        assertTrue(
            schema.get("dateObj")
                .getFacets()
                .getObjectContent()
                .get("myDate")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isDateType()
        );
    }

    @Test
    public void testEnumeration() {
        List<DateItem> values = Arrays.asList(
            createDate("2004-03-12Z"),
            createDate("1945-01-01"),
            createDate("2012-04-12-05:00"),
            createDate("2945-11-01")
        );
        List<Item> enumValues = schema.get("dateType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("dateType").getFacets().getEnumeration().size(), values.size());
        for (DateItem value : values) {
            assertTrue(enumValues.contains(value));
        }

        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("dates").getItem().getItems())
            assertTrue(values.contains((DateItem) itemWrapper.getItem().getItemMap().get("myDate").getItem()));
    }

    private DateItem createDate(String value) {
        DateTime date = DateTimeItem.parseDateTime(value, AtomicTypes.DATE);
        if (!value.endsWith("Z") && date.getZone() == DateTimeZone.getDefault()) {
            return new DateItem(date.withZoneRetainFields(DateTimeZone.UTC), false);
        }
        return new DateItem(date, true);
    }

    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
