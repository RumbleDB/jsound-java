package extendedSchemas.atomicTypes.string.enumeration;

import base.BaseTest;
import jsound.atomicItems.StringItem;
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
            "extendedSchemas/atomicTypes/string/enumerationSchema.json",
            "atomicTypes/string/enumeration/stringEnumeration.json",
            false
        );
    }

    @Test
    public void testSchema() {
        assertTrue(schema.get("stringType").isStringType());
        assertTrue(schema.get("stringObj").isObjectType());
        assertTrue(
            schema.get("stringObj")
                .getFacets()
                .getObjectContent()
                .get("myString")
                .getTypeOrReference()
                .getTypeDescriptor()
                .isStringType()
        );
    }

    @Test
    public void testEnumeration() {
        List<StringItem> values = Arrays.asList(
            new StringItem("first"), new StringItem("second"), new StringItem("third"), new StringItem("fourth")
        );
        List<Item> enumValues = schema.get("stringType")
            .getFacets()
            .getEnumeration()
            .stream()
            .map(ItemWrapper::getItem)
            .collect(
                Collectors.toList()
            );
        assertEquals(schema.get("stringType").getFacets().getEnumeration().size(), values.size());
        for (StringItem value : values) {
            assertTrue(enumValues.contains(value));
        }

        for (ItemWrapper itemWrapper : fileItem.getItem().getItemMap().get("strings").getItem().getItems())
            assertTrue(values.contains((StringItem) itemWrapper.getItem().getItemMap().get("myString").getItem()));
    }

    @Test
    public void testValidate() {
        assertTrue(schemaItem.validate(fileItem, false));
    }
}
