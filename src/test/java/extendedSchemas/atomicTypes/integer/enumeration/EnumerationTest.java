package extendedSchemas.atomicTypes.integer.enumeration;

import base.BaseTest;
import jsound.atomicItems.IntegerItem;
import org.api.Item;
import org.api.ItemWrapper;
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
            "extendedSchemas/atomicTypes/integer/enumerationSchema.json",
            "atomicTypes/integer/enumeration/integerEnumeration.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("integerType").isIntegerType());
        assertTrue(schema.get("integerObj").isObjectType());
        assertTrue(
            schema.get("integerObj")
                .getFacets()
                .getObjectContent()
                .get("myInteger")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isIntegerType()
        );
    }

    @Test
    public void testEnumeration() {
        List<IntegerItem> values = Arrays.asList(
            new IntegerItem(1), new IntegerItem(2), new IntegerItem(3), new IntegerItem(4)
        );
        List<Item> enumValues = schema.get("integerType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("integerType").getFacets().getEnumeration().size(), values.size());
        for (IntegerItem value : values) {
            assertTrue(enumValues.contains(value));
        }

        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("integers").getItem().getItems())
            assertTrue(values.contains((IntegerItem) itemWrapper.getItem().getItemMap().get("myInteger").getItem()));
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
