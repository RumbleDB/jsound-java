package extendedSchemas.atomicTypes.decimal.enumeration;

import base.BaseTest;
import jsound.atomicItems.DecimalItem;
import org.api.Item;
import org.api.ItemWrapper;
import org.api.executors.JSoundExecutor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.api.executors.JSoundExecutor.schema;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnumerationTest extends BaseTest {
    String filePath = "atomicTypes/decimal/enumeration/decimalEnumeration.json";

    @BeforeClass
    public static void initializeApplication() throws IOException {
        jSoundSchema = JSoundExecutor.loadSchemaFromPath(
                schemaPathPrefix + "extendedSchemas/atomicTypes/decimal/enumerationSchema.json",
                "targetType",
                false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("decimalType").isDecimalType());
        assertTrue(schema.get("decimalObj").isObjectType());
        assertTrue(
            schema.get("decimalObj")
                .getFacets()
                .getObjectContent()
                .get("myDecimal")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isDecimalType()
        );
    }

    @Test
    public void testEnumeration() {
        List<DecimalItem> values = Arrays.asList(
            new DecimalItem(BigDecimal.valueOf(1)),
            new DecimalItem(BigDecimal.valueOf(2)),
            new DecimalItem(BigDecimal.valueOf(3.4)),
            new DecimalItem(BigDecimal.valueOf(5e6))
        );
        List<Item> enumValues = schema.get("decimalType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("decimalType").getFacets().getEnumeration().size(), values.size());
        for (DecimalItem value : values) {
            assertTrue(enumValues.contains(value));
        }

        for (ItemWrapper itemWrapper : jSoundSchema.instanceItem.getItem().getItemMap().get("decimals").getItem().getItems())
            assertTrue(values.contains((DecimalItem) itemWrapper.getItem().getItemMap().get("myDecimal").getItem()));
    }

    @Test
    public void testValidate() throws IOException {
        assertTrue(jSoundSchema.validateJSONFromPath(filePathPrefix + filePath));
    }
}
