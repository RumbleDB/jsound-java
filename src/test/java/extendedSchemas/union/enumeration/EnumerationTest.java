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
import org.api.executors.JSoundExecutor;
import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnumerationTest extends BaseTest {
    String filePath = "union/enumeration/unionEnumeration.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/union/enumerationSchema.json",
                "targetType",
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

        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("unions").getItem().getItems())
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
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
