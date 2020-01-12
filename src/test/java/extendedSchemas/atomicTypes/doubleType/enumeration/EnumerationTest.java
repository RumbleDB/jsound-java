package extendedSchemas.atomicTypes.doubleType.enumeration;

import base.BaseTest;
import jsound.atomicItems.DoubleItem;
import org.api.Item;
import org.api.ItemWrapper;
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
            "extendedSchemas/atomicTypes/double/enumerationSchema.json",
            "atomicTypes/double/enumeration/doubleEnumeration.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("doubleType").isDoubleType());
        assertTrue(schema.get("doubleObj").isObjectType());
        assertTrue(
            schema.get("doubleObj")
                .getFacets()
                .getObjectContent()
                .get("myDouble")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isDoubleType()
        );
    }

    @Test
    public void testEnumeration() {
        List<DoubleItem> values = Arrays.asList(
            new DoubleItem(12e3),
            new DoubleItem(3.45),
            new DoubleItem(678d),
            new DoubleItem(9E1)
        );
        List<Item> enumValues = schema.get("doubleType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("doubleType").getFacets().getEnumeration().size(), values.size());
        for (DoubleItem value : values) {
            assertTrue(enumValues.contains(value));
        }

        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("doubles").getItem().getItems())
            assertTrue(values.contains((DoubleItem) itemWrapper.getItem().getItemMap().get("myDouble").getItem()));
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
