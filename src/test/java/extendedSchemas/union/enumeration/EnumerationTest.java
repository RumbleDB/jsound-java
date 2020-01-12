package extendedSchemas.union.enumeration;

import base.BaseTest;
import jsound.atomicItems.DateItem;
import jsound.atomicItems.DateTimeItem;
import jsound.atomicItems.HexBinaryItem;
import jsound.atomicItems.YearMonthDurationItem;
import jsound.types.AtomicTypes;
import jsound.types.ItemTypes;
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
            "extendedSchemas/union/enumerationSchema.json",
            "union/enumeration/unionEnumeration.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("unionType").isUnionType());
        assertTrue(schema.get("unionObj").isObjectType());
        assertTrue(
            schema.get("unionObj")
                .getFacets()
                .getObjectContent()
                .get("myUnion")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isUnionType()
        );
    }

    @Test
    public void testEnumeration() {

        List<Item> values = Arrays.asList(
                new HexBinaryItem(HexBinaryItem.parseHexBinaryString("AAAA"), "AAAA"),
                createDate("2001-12-12Z"),
                new YearMonthDurationItem(getDurationFromString("P12Y", ItemTypes.YEARMONTHDURATION)),
                new YearMonthDurationItem(getDurationFromString("P1Y3M", ItemTypes.YEARMONTHDURATION))
        );
            
        List<Item> enumValues = schema.get("unionType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("unionType").getFacets().getEnumeration().size(), values.size());
        for (Item value : values) {
            assertTrue(enumValues.contains(value));
        }

        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("unions").getItem().getItems())
            assertTrue(values.contains(itemWrapper.getItem().getItemMap().get("myUnion").getItem()));
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
