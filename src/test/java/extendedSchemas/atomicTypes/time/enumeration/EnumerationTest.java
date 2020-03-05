package extendedSchemas.atomicTypes.time.enumeration;

import base.BaseTest;
import jsound.atomicItems.DateTimeItem;
import jsound.atomicItems.TimeItem;
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
    String filePath = "atomicTypes/time/enumeration/timeEnumeration.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/time/enumerationSchema.json",
                "targetType",
                false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("timeType").isTimeType());
        assertTrue(schema.get("timeObj").isObjectType());
        assertTrue(
            schema.get("timeObj")
                .getFacets()
                .getObjectContent()
                .get("myTime")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isTimeType()
        );
    }

    @Test
    public void testEnumeration() {
        List<TimeItem> values = Arrays.asList(
            createTime("13:20:00Z"),
            createTime("00:00:00"),
            createTime("21:12:13+02:00"),
            createTime("19:45:01.011")
        );
        List<Item> enumValues = schema.get("timeType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("timeType").getFacets().getEnumeration().size(), values.size());
        for (TimeItem value : values) {
            assertTrue(enumValues.contains(value));
        }

        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("times").getItem().getItems())
            assertTrue(values.contains((TimeItem) itemWrapper.getItem().getItemMap().get("myTime").getItem()));
    }

    private TimeItem createTime(String value) {
        DateTime time = DateTimeItem.parseDateTime(value, AtomicTypes.TIME);
        if (!value.endsWith("Z") && time.getZone() == DateTimeZone.getDefault()) {
            return new TimeItem(time.withZoneRetainFields(DateTimeZone.UTC), false);
        }
        return new TimeItem(time, true);
    }

    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
