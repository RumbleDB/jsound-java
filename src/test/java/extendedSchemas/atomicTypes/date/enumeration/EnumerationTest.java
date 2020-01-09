package extendedSchemas.atomicTypes.date.enumeration;

import base.BaseTest;
import jsound.atomicItems.DateItem;
import jsound.atomicItems.DateTimeItem;
import jsound.types.AtomicTypes;
import org.api.Item;
import org.api.ItemWrapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
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
            "extendedSchemas/atomicTypes/date/enumerationSchema.json",
            "atomicTypes/date/enumeration/dateEnumeration.json",
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
        List<String> values = Arrays.asList(
                "2004-03-12Z", "1945-01-01", "2012-04-12-05:00", "2945-11-01"
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
        for (String value : values) {
            assertTrue(enumValues.contains(createDate(value)));
        }

        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("dates").getItem().getItems())
            assertTrue(values.contains(itemWrapper.getItem().getItemMap().get("myDate").getItem().getStringValue()));
    }

    private DateItem createDate(String value) {
        DateTime date = DateTimeItem.parseDateTime(value, AtomicTypes.DATE);
        if (!value.endsWith("Z") && date.getZone() == DateTimeZone.getDefault()) {
            return new DateItem(date.withZoneRetainFields(DateTimeZone.UTC), false);
        }
        return new DateItem(date, true);
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
