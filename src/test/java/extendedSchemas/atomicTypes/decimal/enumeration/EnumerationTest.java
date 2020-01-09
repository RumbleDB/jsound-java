package extendedSchemas.atomicTypes.decimal.enumeration;

import base.BaseTest;
import jsound.atomicItems.DecimalItem;
import org.api.Item;
import org.api.ItemWrapper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
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
            "extendedSchemas/atomicTypes/decimal/enumerationSchema.json",
            "atomicTypes/decimal/enumeration/decimalEnumeration.json",
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
                new DecimalItem(BigDecimal.valueOf(5e6)));
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

        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("decimals").getItem().getItems())
            assertTrue(values.contains((DecimalItem) itemWrapper.getItem().getItemMap().get("myDecimal").getItem()));
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
