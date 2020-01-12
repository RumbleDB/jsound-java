package extendedSchemas.atomicTypes.nullType.enumeration;

import base.BaseTest;
import jsound.atomicItems.NullItem;
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
            "extendedSchemas/atomicTypes/null/enumerationSchema.json",
            "atomicTypes/null/enumeration/nullEnumeration.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("nullType").isNullType());
        assertTrue(schema.get("nullObj").isObjectType());
        assertTrue(
            schema.get("nullObj")
                .getFacets()
                .getObjectContent()
                .get("myNull")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isNullType()
        );
    }

    @Test
    public void testEnumeration() {
        List<NullItem> values = Arrays.asList(
            new NullItem(),
            new NullItem()
        );
        List<Item> enumValues = schema.get("nullType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("nullType").getFacets().getEnumeration().size(), values.size());
        for (NullItem value : values) {
            assertTrue(enumValues.contains(value));
        }

        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("nulls").getItem().getItems())
            assertTrue(values.contains((NullItem) itemWrapper.getItem().getItemMap().get("myNull").getItem()));
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
