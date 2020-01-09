package extendedSchemas.atomicTypes.booleanType.enumeration;

import base.BaseTest;
import jsound.atomicItems.BooleanItem;
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
            "extendedSchemas/atomicTypes/boolean/enumerationSchema.json",
            "atomicTypes/boolean/enumeration/booleanEnumeration.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("booleanType").isBooleanType());
        assertTrue(schema.get("booleanObj").isObjectType());
        assertTrue(
            schema.get("booleanObj")
                .getFacets()
                .getObjectContent()
                .get("myBoolean")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isBooleanType()
        );
    }

    @Test
    public void testEnumeration() {
        List<String> values = Arrays.asList(
            "true", "false", "true", "false"
        );
        List<Item> enumValues = schema.get("booleanType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("booleanType").getFacets().getEnumeration().size(), values.size());
        for (String value : values) {
            assertTrue(enumValues.contains(new BooleanItem(Boolean.parseBoolean(value))));
        }

        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("booleans").getItem().getItems())
            assertTrue(values.contains(itemWrapper.getItem().getItemMap().get("myBoolean").getItem().getStringValue()));
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
